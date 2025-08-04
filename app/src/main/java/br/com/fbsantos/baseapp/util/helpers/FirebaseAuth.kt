package br.com.fbsantos.baseapp.util.helpers

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

/**
 * Helper para autenticação com FirebaseUI Auth no Jetpack Compose.
 *
 * Centraliza:
 * - A inicialização e o disparo do fluxo de login usando o launcher de resultados do Compose.
 * - A leitura do ID token (force refresh) do usuário autenticado.
 * - O logout do usuário atual.
 *
 * Pré-requisitos (resumo):
 * 1) Firebase Console: app com package e SHA-1 corretos.
 * 2) Gradle: dependência `com.firebaseui:firebase-ui-auth` e plugin `google-services`.
 * 3) `google-services.json` na pasta /app.
 *
 * Observação: o nome deste helper colide com `com.google.firebase.auth.FirebaseAuth`.
 * Para evitar ambiguidade, as chamadas à classe do Firebase usam o nome totalmente qualificado.
 */
object FirebaseAuth {
    /**
     * Prepara e retorna um lambda que inicia o fluxo de autenticação do FirebaseUI.
     *
     * Comportamento:
     * - Cria um launcher para receber o resultado do fluxo de login.
     * - Ao chamar o lambda retornado, limpa o estado anterior (`AuthUI.signOut`) e inicia o sign-in.
     * - Em sucesso, solicita o ID token atualizado e retorna via [onSuccess].
     * - Em erro ou cancelamento, retorna mensagem apropriada via [onError].
     *
     * Importante:
     * - Esta função é @Composable e deve ser chamada dentro de uma composição.
     * - O `context` deve ser válido para uso com `AuthUI` (geralmente o contexto da Activity/Compose).
     *
     * @param context Contexto usado para signOut e criação do intent de login.
     * @param onSuccess Callback com `(idToken, FirebaseUser)` quando a autenticação for concluída.
     * @param onError Callback com mensagem de erro em falhas, cancelamentos ou impossibilidade de obter token.
     * @return Função sem parâmetros que, quando invocada, dispara o fluxo de login (launcher.launch).
     */
    @Composable
    fun auth(
        context: Context,
        onSuccess: (String, FirebaseUser) -> Unit,
        onError: (String) -> Unit
    ): () -> Unit {
        val launcher = rememberLauncherForActivityResult(
            contract = FirebaseAuthUIActivityResultContract()
        ) { result: FirebaseAuthUIAuthenticationResult ->
            val response = result.idpResponse
            if (result.resultCode == Activity.RESULT_OK) {
                getIdToken { idToken, user ->
                    if(idToken != null && user != null) {
                        onSuccess(idToken, user)
                    } else{
                        onError("Não foi possível obter o token de autenticação. Tente novamente.")
                    }
                }
            } else {
                val mensagem = response?.error?.message
                    ?: "Ocorreu um erro ao fazer login. Verifique sua conexão ou tente mais tarde."
                onError(mensagem)
            }
        }

        return {
            AuthUI.getInstance().signOut(context).addOnCompleteListener {
                val providers = listOf(AuthUI.IdpConfig.GoogleBuilder().build())

                val signInIntent: Intent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .setCredentialManagerEnabled(false)
                    .build()

                launcher.launch(signInIntent)
            }
        }
    }

    /**
     * Efetua logout do usuário atual do Firebase Authentication.
     *
     * Observação: utiliza `com.google.firebase.auth.FirebaseAuth` diretamente (nome totalmente qualificado)
     * para evitar colisão com o nome deste helper.
     */
    fun logout() {
        FirebaseAuth.getInstance().signOut()
    }

    /**
     * Obtém um ID token atualizado (force refresh) do usuário autenticado.
     *
     * Comportamento:
     * - Se houver usuário autenticado, chama `getIdToken(true)` e retorna `(token, user)` em sucesso.
     * - Em falha ou ausência de usuário, retorna `(null, null)`.
     *
     * @param callback Função chamada com `(idToken, FirebaseUser)` ou `(null, null)` em caso de falha/sem usuário.
     */
    fun getIdToken(callback: (String?, FirebaseUser?) -> Unit) {
        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            user.getIdToken(true)
                .addOnSuccessListener { result ->
                    val idToken = result.token
                    callback(idToken, user)
                }
                .addOnFailureListener {
                    callback(null, null)
                }
        } else {
            callback(null, null)
        }
    }
}