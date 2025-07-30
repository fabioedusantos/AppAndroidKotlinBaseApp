package br.com.fbsantos.baseapp.ui.screen.auth.recuperarconta

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import br.com.fbsantos.baseapp.ui.screen.auth.recuperarconta.content.RecuperarCodigoConfirmarCodigoContent
import br.com.fbsantos.baseapp.ui.screen.auth.recuperarconta.content.RecuperarCodigoInicioContent
import br.com.fbsantos.baseapp.ui.screen.auth.recuperarconta.content.RecuperarCodigoResetarSenhaContent
import br.com.fbsantos.baseapp.ui.screen.auth.recuperarconta.content.RecuperarCodigoSucessoContent
import br.com.fbsantos.baseapp.util.helpers.AnimatedManager
import org.koin.androidx.compose.koinViewModel

@Composable
fun RecuperarContaScreen(
    navController: NavController,
    viewModel: RecuperarContaViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState.collectAsState().value
    AnimatedManager.Switcher(
        targetState = uiState.etapa
    ) { etapa ->
        when (etapa) {
            EtapaRecuperarContaEnum.INICIAL -> RecuperarCodigoInicioContent(
                state = uiState,
                onEmailChange = { viewModel.onEmailChange(it) },
                onEnviarCodigo = { recaptchaToken, recaptchaSiteKey ->
                    viewModel.onEnviarCodigo(
                        recaptchaToken,
                        recaptchaSiteKey
                    )
                },
                setFormEnabled = { viewModel.setFormEnabled(it) },
                setError = { viewModel.setError(it) },
            )

            EtapaRecuperarContaEnum.EMAIL_ENVIADO -> RecuperarCodigoConfirmarCodigoContent(
                state = uiState,
                onCodigoChange = { viewModel.onCodigoChange(it) },
                onChecarCodigo = { recaptchaToken, recaptchaSiteKey ->
                    viewModel.onChecarCodigo(
                        recaptchaToken,
                        recaptchaSiteKey
                    )
                },
                onReenviarCodigo = { recaptchaToken, recaptchaSiteKey ->
                    viewModel.onReenviarCodigo(
                        recaptchaToken,
                        recaptchaSiteKey
                    )
                },
                setIsFocarCodigo = { viewModel.setIsFocarCodigo(it) },
                setFormEnabled = { viewModel.setFormEnabled(it) },
                setError = { viewModel.setError(it) }
            )

            EtapaRecuperarContaEnum.CODIGO_VALIDADO -> RecuperarCodigoResetarSenhaContent(
                state = uiState,
                onSenhaChange = { viewModel.onSenhaChange(it) },
                onSenhaVisivelToggle = { viewModel.onSenhaVisivelToggle() },
                onConfirmeSenhaChange = { viewModel.onConfirmeSenhaChange(it) },
                onConfirmeSenhaVisivelToggle = { viewModel.onConfirmeSenhaVisivelToggle() },
                onResetarSenha = { recaptchaToken, recaptchaSiteKey ->
                    viewModel.onResetarSenha(
                        recaptchaToken,
                        recaptchaSiteKey
                    )
                },
                setFormEnabled = { viewModel.setFormEnabled(it) },
                setError = { viewModel.setError(it) }
            )

            EtapaRecuperarContaEnum.SUCESSO -> RecuperarCodigoSucessoContent(navController)
        }
    }

}