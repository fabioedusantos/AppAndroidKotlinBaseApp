package br.com.fbsantos.ui.auth.criarconta.content

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fbsantos.baseapp.R
import br.com.fbsantos.baseapp.ui.components.ErrorTextWithFocus
import br.com.fbsantos.baseapp.ui.components.container.AuthContainer
import br.com.fbsantos.baseapp.ui.theme.BaseAppTheme
import br.com.fbsantos.baseapp.util.FirebaseAuthHelper
import br.com.fbsantos.baseapp.util.RecaptchaHelper
import br.com.fbsantos.di.appModule
import br.com.fbsantos.ui.auth.criarconta.CriarContaUiState
import com.google.firebase.auth.FirebaseUser
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CriarContaInicio(
    navController: NavController,
    state: CriarContaUiState,
    onNomeChange: (String) -> Unit,
    onSobreNomeChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onSenhaChange: (String) -> Unit,
    onSenhaVisivelToggle: () -> Unit,
    onConfirmeSenhaChange: (String) -> Unit,
    onConfirmeSenhaVisivelToggle: () -> Unit,
    onTermosAceitosChange: (Boolean) -> Unit,
    onPoliticaAceitaChange: (Boolean) -> Unit,
    onCriarConta: (String, String) -> Unit,
    onCriarContaByGoogle: (String, FirebaseUser) -> Unit,
    setFormEnabled: (Boolean) -> Unit,
    setError: (String) -> Unit
) {
    AuthContainer (
        fixedBottomContent = {
            Button(
                onClick = {
                    RecaptchaHelper.exec(
                        before = { setFormEnabled(false) },
                        after = { setFormEnabled(true) },
                        onSuccess = { token, siteKey ->
                            onCriarConta(token, siteKey)
                        },
                        onError = { erro ->
                            setError(erro)
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = state.isFormEnabled
            ) {
                Text("Criar minha conta")
            }
        }
    ) {
        Text("Crie sua conta", textAlign = TextAlign.Center, fontSize = 28.sp)

        val loginGoogle = FirebaseAuthHelper.auth(
            context = LocalContext.current,
            onSuccess = { idToken, user ->
                RecaptchaHelper.exec(
                    before = { setFormEnabled(false) },
                    after = { setFormEnabled(true) },
                    onSuccess = { token, siteKey ->
                        onCriarContaByGoogle(idToken, user)
                    },
                    onError = { erro ->
                        setError(erro)
                    }
                )
            },
            onError = { erro ->
                setError(erro)
            }
        )

        OutlinedButton(
            onClick = loginGoogle,
            modifier = Modifier.fillMaxWidth(),
            enabled = state.isFormEnabled && state.isGoogleButtonEnabled
        ) {
            Image(
                painter = painterResource(id = R.drawable.android_light_rd),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Continuar com Google")
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            OutlinedTextField(
                value = state.nome,
                onValueChange = onNomeChange,
                label = { Text("Nome") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                isError = state.isNomeError,
                supportingText = {
                    if (state.isNomeError) Text(state.nomeErrorText)
                },
                enabled = state.isFormEnabled
            )

            OutlinedTextField(
                value = state.sobrenome,
                onValueChange = onSobreNomeChange,
                label = { Text("Sobrenome") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                isError = state.isSobrenomeError,
                supportingText = {
                    if (state.isSobrenomeError) Text(state.sobrenomeErrorText)
                },
                enabled = state.isFormEnabled
            )
        }

        OutlinedTextField(
            value = state.email,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = state.isEmailError,
            supportingText = {
                if (state.isEmailError) Text(state.emailErrorText)
            },
            enabled = (state.isFormEnabled && state.isEmailEnabled)
        )

        if (state.isSenhasInputVisible) {
            OutlinedTextField(
                value = state.senha,
                onValueChange = onSenhaChange,
                label = { Text("Senha") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (state.isSenhaVisivel) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon =
                        if (state.isSenhaVisivel) Icons.Default.VisibilityOff else Icons.Default.Visibility
                    IconButton(onClick = onSenhaVisivelToggle) {
                        Icon(imageVector = icon, contentDescription = null)
                    }
                },
                isError = state.isSenhaError,
                supportingText = {
                    if (state.isSenhaError) Text(state.senhaErrorText)
                },
                enabled = state.isFormEnabled
            )

            OutlinedTextField(
                value = state.confirmeSenha,
                onValueChange = onConfirmeSenhaChange,
                label = { Text("Confirme sua senha") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (state.isConfirmeSenhaVisivel) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon =
                        if (state.isConfirmeSenhaVisivel) Icons.Default.VisibilityOff else Icons.Default.Visibility
                    IconButton(onClick = onConfirmeSenhaVisivelToggle) {
                        Icon(imageVector = icon, contentDescription = null)
                    }
                },
                isError = state.isConfirmeSenhaError,
                enabled = state.isFormEnabled
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = state.isTermosAceitos,
                onCheckedChange = onTermosAceitosChange,
                enabled = state.isFormEnabled
            )

            Text(
                text = "Aceito os termos e condições de uso",
                color = MaterialTheme.colorScheme.primary,
                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                lineHeight = 18.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        enabled = state.isFormEnabled,
                        onClick = {
                            //TODO CRIAR TERMOS E CONDIÇÕES NavHelper.abrir(navController, Routes.TermosCondicoes.route)
                        }
                    )
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = state.isPoliticaAceita,
                onCheckedChange = onPoliticaAceitaChange,
                enabled = state.isFormEnabled
            )

            Text(
                text = "Aceito a política de privacidade",
                color = MaterialTheme.colorScheme.primary,
                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                lineHeight = 18.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        enabled = state.isFormEnabled,
                        onClick = {
                            //TODO CRIAR POLÍTICA DE PRIVACIDADE Nav.abrir(navController, Routes.PoliticaPrivacidade.route)
                        }
                    )
            )
        }

        ErrorTextWithFocus(state.error)
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun CriarContaInicioPreview() {
    val context = LocalContext.current
    startKoin {
        androidContext(context)
        modules(appModule)
    }

    val navController = rememberNavController()

    val previewState = CriarContaUiState(
        nome = "Fábio",
        sobrenome = "Santos",
        email = "fabioedusantos@gmail.com",
        senha = "12345678@A",
        confirmeSenha = "12345678@A",
        isTermosAceitos = true,
        isPoliticaAceita = true
    )

    BaseAppTheme (darkTheme = false) {
        CriarContaInicio(
            navController = navController,
            state = previewState,
            onNomeChange = {},
            onSobreNomeChange = {},
            onEmailChange = {},
            onSenhaChange = {},
            onSenhaVisivelToggle = {},
            onConfirmeSenhaChange = {},
            onConfirmeSenhaVisivelToggle = {},
            onTermosAceitosChange = {},
            onPoliticaAceitaChange = {},
            onCriarConta = { _, _ -> },
            onCriarContaByGoogle = { _, _ -> },
            setFormEnabled = {},
            setError = {}
        )
    }
}