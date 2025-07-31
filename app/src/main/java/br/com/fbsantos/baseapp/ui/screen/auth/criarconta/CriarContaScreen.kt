package br.com.fbsantos.baseapp.ui.screen.auth.criarconta

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import br.com.fbsantos.baseapp.util.helpers.AnimatedManager
import br.com.fbsantos.baseapp.ui.screen.auth.criarconta.content.CriarContaCodigoContent
import br.com.fbsantos.baseapp.ui.screen.auth.criarconta.content.CriarContaInicioContent
import br.com.fbsantos.baseapp.ui.screen.auth.criarconta.content.CriarContaSucessoContent
import org.koin.androidx.compose.koinViewModel

@Composable
fun CriarContaScreen(
    navController: NavController,
    viewModel: CriarContaViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState.collectAsState().value

    AnimatedManager.Switcher(
        targetState = uiState.etapa
    ) { etapa ->
        when (etapa) {
            CriarContaEtapaEnum.INICIO -> CriarContaInicioContent(
                navController = navController,
                state = uiState,
                onNomeChange = { viewModel.onNomeChange(it) },
                onSobreNomeChange = { viewModel.onSobreNomeChange(it) },
                onEmailChange = { viewModel.onEmailChange(it) },
                onSenhaChange = { viewModel.onSenhaChange(it) },
                onSenhaVisivelToggle = { viewModel.onSenhaVisivelToggle() },
                onConfirmeSenhaChange = { viewModel.onConfirmeSenhaChange(it) },
                onConfirmeSenhaVisivelToggle = { viewModel.onConfirmeSenhaVisivelToggle() },
                onTermosAceitosChange = { viewModel.onTermosAceitosChange(it) },
                onPoliticaAceitaChange = { viewModel.onPoliticaAceitaChange(it) },
                onCriarConta = { recaptchaToken, recaptchaSiteKey ->
                    viewModel.onCriarConta(
                        recaptchaToken,
                        recaptchaSiteKey
                    )
                },
                onCriarContaByGoogle = { idTokenFirebase, userFirebase ->
                    viewModel.onCriarContaByGoogle(
                        idTokenFirebase,
                        userFirebase
                    )
                },
                setError = { viewModel.setError(it) },
                onWaitMessage = { viewModel.onWaitMessage() },
                onClearWaitMessage = { viewModel.onClearWaitMessage() }
            )

            CriarContaEtapaEnum.VALIDAR_CODIGO -> CriarContaCodigoContent(
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
                setFocarCodigo = { viewModel.setFocarCodigo(it) },
                setError = { viewModel.setError(it) },
                onWaitMessage = { viewModel.onWaitMessage() },
                onClearWaitMessage = { viewModel.onClearWaitMessage() }
            )

            CriarContaEtapaEnum.SUCESSO -> CriarContaSucessoContent(navController)
        }
    }
}