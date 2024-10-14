package br.com.fbsantos.baseapp.util

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
 * Para usar este Helper:
 * 1. No Firebase Console → Configurações do Projeto → Seus aplicativos:
 *    - Adicione o app com packageName e SHA-1 corretos.
 *    - SHA-1: `./gradlew signingReport` (usar debug/release conforme necessário).
 * 2. No build.gradle (app):
 *    plugins { id("com.google.gms.google-services") version "4.4.3" apply false }
 *    dependencies { implementation "com.firebaseui:firebase-ui-auth:9.0.0" }
 * 3. Baixe o google-services.json, coloque na pasta /app e adicione ao .gitignore.
 */
object FirebaseAuthHelper {
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
                    .build()

                launcher.launch(signInIntent)
            }
        }
    }

    fun logout() {
        FirebaseAuth.getInstance().signOut()
    }

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