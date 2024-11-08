package br.com.fbsantos.baseapp.ui.screen.auth.login.content

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fbsantos.baseapp.R
import br.com.fbsantos.baseapp.config.navigation.Routes
import br.com.fbsantos.baseapp.ui.components.ErrorTextWithFocus
import br.com.fbsantos.baseapp.ui.components.container.AuthContainer
import br.com.fbsantos.baseapp.ui.screen.auth.login.LoginUiState
import br.com.fbsantos.baseapp.ui.theme.BaseAppTheme
import br.com.fbsantos.baseapp.util.FirebaseAuthHelper
import br.com.fbsantos.baseapp.util.NavHelper
import br.com.fbsantos.baseapp.util.RecaptchaHelper
import br.com.fbsantos.baseapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@Composable
fun LoginSeletorContent(
    navController: NavController,
    state: LoginUiState,
    setLoginEmailSenha: (Boolean) -> Unit,
    setFormEnabled: (Boolean) -> Unit,
    onLoginByGoogleClicked: (String, String, String) -> Unit,
    setError: (String) -> Unit
) {
    AuthContainer {
        Text("Bem-vindo", fontSize = 28.sp, color = MaterialTheme.colorScheme.onSurface)

        val loginGoogle = FirebaseAuthHelper.auth(
            context = LocalContext.current,
            onSuccess = { idToken, user ->
                RecaptchaHelper.exec(
                    before = { setFormEnabled(false) },
                    after = { setFormEnabled(true) },
                    onSuccess = { token, siteKey ->
                        onLoginByGoogleClicked(idToken, token, siteKey)
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
            shape = RoundedCornerShape(50),
            enabled = state.isFormEnabled
        ) {
            Image(
                painter = painterResource(id = R.drawable.android_light_rd),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Entrar com Google")
        }

        Button(
            onClick = {
                setLoginEmailSenha(true)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
            enabled = state.isFormEnabled
        ) {
            Text("Entrar com email e senha")
        }

        Button(
            onClick = {
                NavHelper.abrir(navController, Routes.CriarConta.route)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary
            ),
            enabled = state.isFormEnabled
        ) {
            Text("Criar minha conta")
        }

        ErrorTextWithFocus(state.error)
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun LoginSeletorPreview() {
    val navController = rememberNavController()

    val previewState = LoginUiState(
        isFormEnabled = true
    )

    BaseAppTheme (darkTheme = false) {
        LoginSeletorContent(
            navController = navController,
            state = previewState,
            setLoginEmailSenha = {},
            setFormEnabled = {},
            onLoginByGoogleClicked = { _, _, _ -> },
            setError = {}
        )
    }
}