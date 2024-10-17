package br.com.fbsantos.ui.auth.criarconta

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import br.com.fbsantos.baseapp.util.AnimatedHelper
import br.com.fbsantos.ui.auth.criarconta.content.CriarContaConfirmarCodigo
import br.com.fbsantos.ui.auth.criarconta.content.CriarContaInicio
import br.com.fbsantos.ui.auth.criarconta.content.CriarContaSucesso
import org.koin.androidx.compose.koinViewModel

@Composable
fun CriarContaScreen(
    navController: NavController,
    viewModel: CriarContaViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState.collectAsState().value

    AnimatedHelper.Switcher(
        targetState = uiState.etapa
    ) { etapa ->
        when (etapa) {
            EtapaCriarConta.INICIAL -> CriarContaInicio(
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
                setFormEnabled = { viewModel.setFormEnabled(it) },
                setError = { viewModel.setError(it) },
            )

            EtapaCriarConta.EMAIL_ENVIADO -> CriarContaConfirmarCodigo(
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
                setFormEnabled = { viewModel.setFormEnabled(it) },
                setError = { viewModel.setError(it) },
            )

            EtapaCriarConta.CODIGO_VALIDADO -> CriarContaSucesso(navController)
        }
    }
}