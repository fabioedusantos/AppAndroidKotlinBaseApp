package br.com.fbsantos.baseapp.ui.screen.auth.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fbsantos.baseapp.ui.theme.BaseAppTheme
import br.com.fbsantos.baseapp.util.helpers.AnimatedManager
import br.com.fbsantos.baseapp.di.appModule
import br.com.fbsantos.baseapp.ui.screen.auth.login.content.LoginEmailSenhaContent
import br.com.fbsantos.baseapp.ui.screen.auth.login.content.LoginSeletorContent
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.koinViewModel
import org.koin.core.context.startKoin

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState.collectAsState().value

    AnimatedManager.Switcher(
        targetState = uiState.isLoginEmailSenha
    ) { isLoginEmailSenha ->
        if (!isLoginEmailSenha) {
            LoginSeletorContent(
                navController = navController,
                state = uiState,
                setLoginEmailSenha = { viewModel.setLoginEmailSenha(it) },
                setFormEnabled = { viewModel.setFormEnabled(it) },
                onLoginByGoogleClicked = { idToken, recaptchaToken, recaptchaSiteKey ->
                    viewModel.onLoginByGoogle(
                        idToken,
                        recaptchaToken,
                        recaptchaSiteKey
                    )
                },
                setError = { viewModel.setError(it) }
            )
        } else {
            LoginEmailSenhaContent(
                navController = navController,
                state = uiState,
                setExibirEmailSenha = { viewModel.setLoginEmailSenha(it) },
                onEmailChange = { viewModel.onEmailChanged(it) },
                onSenhaChange = { viewModel.onSenhaChanged(it) },
                onSenhaVisivelToggle = { viewModel.onSenhaVisivelToggle() },
                onLoginClicked = { recaptchaToken, recaptchaSiteKey ->
                    viewModel.onLogin(
                        recaptchaToken,
                        recaptchaSiteKey
                    )
                },
                setFormEnabled = { viewModel.setFormEnabled(it) },
                setError = { viewModel.setError(it) }
            )
        }
    }
}



@Preview(showSystemUi = true, showBackground = true)
@Composable
fun LoginScreenPreview() {
    val context = LocalContext.current
    startKoin {
        androidContext(context)
        modules(appModule)
    }

    val navController = rememberNavController()

    BaseAppTheme (darkTheme = true) {
        LoginScreen(navController)
    }
}