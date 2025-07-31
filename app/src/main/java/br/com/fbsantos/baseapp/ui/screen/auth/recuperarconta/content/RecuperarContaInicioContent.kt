package br.com.fbsantos.baseapp.ui.screen.auth.recuperarconta.content

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import br.com.fbsantos.baseapp.ui.components.ErrorTextWithFocus
import br.com.fbsantos.baseapp.ui.components.container.AuthContainer
import br.com.fbsantos.baseapp.ui.theme.BaseAppTheme
import br.com.fbsantos.baseapp.util.helpers.Recaptcha
import br.com.fbsantos.baseapp.ui.screen.auth.recuperarconta.RecuperarContaUiState

@Composable
fun RecuperarContaInicioContent(
    state: RecuperarContaUiState,
    onEmailChange: (String) -> Unit,
    onEnviarCodigo: (String, String) -> Unit,
    setError: (String) -> Unit,
    onWaitMessage: () -> Unit,
    onClearWaitMessage: () -> Unit
) {
    AuthContainer {
        Text("Recuperar conta", fontSize = 28.sp)

        Text(
            "Se encontrarmos sua conta, você receberá um e-mail com um código. No próximo " +
                    "passo, informe esse código para redefinir sua senha.",
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

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
            enabled = state.isFormEnabled
        )

        Button(
            onClick = {
                onWaitMessage()
                Recaptcha.exec(
                    onSuccess = { token, siteKey ->
                        onEnviarCodigo(token, siteKey)
                    },
                    onError = { erro ->
                        setError(erro)
                        onClearWaitMessage()
                    }
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = state.isFormEnabled
        ) {
            Text(state.messageWait?: "Próximo passo...")
        }

        ErrorTextWithFocus(state.error)
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun RecuperarContaInicioPreview() {
    val previewState = RecuperarContaUiState(
        email = "fabioedusantos@gmail.com"
    )

    BaseAppTheme (darkTheme = false) {
        RecuperarContaInicioContent(
            state = previewState,
            onEmailChange = {},
            onEnviarCodigo = { _, _ -> },
            setError = {},
            onWaitMessage = {},
            onClearWaitMessage = {}
        )
    }
}