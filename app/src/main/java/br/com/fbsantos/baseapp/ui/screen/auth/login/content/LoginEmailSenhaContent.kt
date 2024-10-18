package br.com.fbsantos.baseapp.ui.screen.auth.login.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fbsantos.baseapp.config.navigation.Routes
import br.com.fbsantos.baseapp.ui.components.ErrorTextWithFocus
import br.com.fbsantos.baseapp.ui.components.container.AuthContainer
import br.com.fbsantos.baseapp.ui.screen.auth.login.LoginUiState
import br.com.fbsantos.baseapp.ui.theme.BaseAppTheme
import br.com.fbsantos.baseapp.util.NavHelper
import br.com.fbsantos.baseapp.util.RecaptchaHelper
import br.com.fbsantos.baseapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@Composable
fun LoginEmailSenhaContent(
    navController: NavController,
    state: LoginUiState,
    setExibirEmailSenha: (Boolean) -> Unit,
    onEmailChange: (String) -> Unit,
    onSenhaChange: (String) -> Unit,
    onSenhaVisivelToggle: () -> Unit,
    onLoginClicked: (String, String) -> Unit,
    setFormEnabled: (Boolean) -> Unit,
    setError: (String) -> Unit
) {
    AuthContainer {
        Text("Bem-vindo", fontSize = 28.sp, color = MaterialTheme.colorScheme.onSurface)

        OutlinedTextField(
            value = state.email,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = state.isEmailError,
            supportingText = {
                if (state.isEmailError) Text(state.emailErrorText)
            },
            onValueChange = { onEmailChange(it) },
            enabled = state.isFormEnabled
        )

        OutlinedTextField(
            value = state.senha,
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
            onValueChange = { onSenhaChange(it) },
            enabled = state.isFormEnabled
        )

        Text(
            text = "Esqueci a senha",
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
            fontSize = 14.sp,
            modifier = Modifier
                .align(Alignment.End)
                .clickable(
                    enabled = state.isFormEnabled,
                    onClick = {
                        NavHelper.abrir(navController, Routes.RecuperarConta.route)
                    }
                )
        )

        ErrorTextWithFocus(state.error)

        Button(
            onClick = {
                RecaptchaHelper.exec(
                    before = { setFormEnabled(false) },
                    after = { setFormEnabled(true) },
                    onSuccess = { token, siteKey ->
                        onLoginClicked(token, siteKey)
                    },
                    onError = { erro ->
                        setError(erro)
                    }
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            enabled = state.isFormEnabled
        ) {
            Text("Entrar")
        }

        OutlinedButton(
            onClick = {
                setExibirEmailSenha(false)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50),
            enabled = state.isFormEnabled
        ) {
            Text("Escolher como logar")
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun LoginEmailSenhaPreview() {
    val context = LocalContext.current
    startKoin {
        androidContext(context)
        modules(appModule)
    }

    val navController = rememberNavController()

    val previewState = LoginUiState(
        email = "teste@email.com", senha = "123456", isSenhaVisivel = true, isFormEnabled = true
    )

    BaseAppTheme (darkTheme = false) {
        LoginEmailSenhaContent(
            navController = navController,
            state = previewState,
            setExibirEmailSenha = {},
            onEmailChange = {},
            onSenhaChange = {},
            onSenhaVisivelToggle = {},
            onLoginClicked = { _, _ -> },
            setFormEnabled = {},
            setError = {}
        )
    }
}