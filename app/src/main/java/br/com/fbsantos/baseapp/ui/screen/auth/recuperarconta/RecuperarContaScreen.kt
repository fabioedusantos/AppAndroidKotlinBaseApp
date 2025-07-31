package br.com.fbsantos.baseapp.ui.screen.auth.recuperarconta

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import br.com.fbsantos.baseapp.ui.screen.auth.recuperarconta.content.RecuperarContaCodigoContent
import br.com.fbsantos.baseapp.ui.screen.auth.recuperarconta.content.RecuperarContaInicioContent
import br.com.fbsantos.baseapp.ui.screen.auth.recuperarconta.content.RecuperarContaSenhaContent
import br.com.fbsantos.baseapp.ui.screen.auth.recuperarconta.content.RecuperarContaSucessoContent
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
            RecuperarContaEtapaEnum.INICIO -> RecuperarContaInicioContent(
                state = uiState,
                onEmailChange = { viewModel.onEmailChange(it) },
                onEnviarCodigo = { recaptchaToken, recaptchaSiteKey ->
                    viewModel.onEnviarCodigo(
                        recaptchaToken,
                        recaptchaSiteKey
                    )
                },
                setError = { viewModel.setError(it) },
                onWaitMessage = { viewModel.onWaitMessage() },
                onClearWaitMessage = { viewModel.onClearWaitMessage() },
            )

            RecuperarContaEtapaEnum.VALIDAR_CODIGO -> RecuperarContaCodigoContent(
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
                setError = { viewModel.setError(it) },
                onWaitMessage = { viewModel.onWaitMessage() },
                onClearWaitMessage = { viewModel.onClearWaitMessage() }
            )

            RecuperarContaEtapaEnum.ALTERAR_SENHA -> RecuperarContaSenhaContent(
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
                setError = { viewModel.setError(it) },
                onWaitMessage = { viewModel.onWaitMessage() },
                onClearWaitMessage = { viewModel.onClearWaitMessage() }
            )

            RecuperarContaEtapaEnum.SUCESSO -> RecuperarContaSucessoContent(navController)
        }
    }

}