package br.com.fbsantos.baseapp.ui.screen.auth.recuperarconta.content

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import br.com.fbsantos.baseapp.ui.components.ErrorTextWithFocus
import br.com.fbsantos.baseapp.ui.components.container.AuthContainer
import br.com.fbsantos.baseapp.ui.screen.auth.recuperarconta.EtapaRecuperarContaEnum
import br.com.fbsantos.baseapp.ui.theme.BaseAppTheme
import br.com.fbsantos.baseapp.util.RecaptchaHelper
import br.com.fbsantos.baseapp.di.appModule
import br.com.fbsantos.baseapp.ui.screen.auth.recuperarconta.RecuperarContaUiState
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@Composable
fun RecuperarCodigoResetarSenhaContent(
    state: RecuperarContaUiState,
    onSenhaChange: (String) -> Unit,
    onSenhaVisivelToggle: () -> Unit,
    onConfirmeSenhaChange: (String) -> Unit,
    onConfirmeSenhaVisivelToggle: () -> Unit,
    onResetarSenha: (String, String) -> Unit,
    setFormEnabled: (Boolean) -> Unit,
    setError: (String) -> Unit,
) {
    AuthContainer {
        Text("Recuperar conta", fontSize = 28.sp)

        Text(
            "Digite sua nova senha para recuperar o acesso a sua conta.",
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.senha,
            onValueChange = onSenhaChange,
            label = { Text("Nova Senha") },
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

        ErrorTextWithFocus(state.error)

        Button(
            onClick = {
                RecaptchaHelper.exec(
                    before = { setFormEnabled(false) },
                    after = { setFormEnabled(true) },
                    onSuccess = { token, siteKey ->
                        onResetarSenha(token, siteKey)
                    },
                    onError = { erro ->
                        setError(erro)
                    }
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = state.isFormEnabled
        ) {
            Text("Alterar minha senha!")
        }
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun RecuperarCodigoResetarSenhaContentPreview() {
    val context = LocalContext.current
    startKoin {
        androidContext(context)
        modules(appModule)
    }

    val previewState = RecuperarContaUiState(
        etapa = EtapaRecuperarContaEnum.INICIAL,
        email = "fabioedusantos@gmail.com",
        senha = "12345678@A",
        isSenhaVisivel = true,
        confirmeSenha = "12345678@A",
        isConfirmeSenhaVisivel = true
    )

    BaseAppTheme (darkTheme = false) {
        RecuperarCodigoResetarSenhaContent(
            state = previewState,
            onSenhaChange = {},
            onSenhaVisivelToggle = {},
            onConfirmeSenhaChange = {},
            onConfirmeSenhaVisivelToggle = {},
            onResetarSenha = { _, _ -> },
            setFormEnabled = {},
            setError = {}
        )
    }
}