package br.com.fbsantos.baseapp.ui.screen.auth.criarconta.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fbsantos.baseapp.config.AppConfig
import br.com.fbsantos.baseapp.ui.components.ErrorTextWithFocus
import br.com.fbsantos.baseapp.ui.components.container.AuthContainer
import br.com.fbsantos.baseapp.ui.theme.BaseAppTheme
import br.com.fbsantos.baseapp.util.RecaptchaHelper
import br.com.fbsantos.baseapp.util.Utils
import br.com.fbsantos.baseapp.di.appModule
import br.com.fbsantos.baseapp.ui.screen.auth.criarconta.CriarContaUiState
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@Composable
fun CriarContaConfirmarCodigo(
    state: CriarContaUiState,
    onCodigoChange: (MutableList<String>) -> Unit,
    onChecarCodigo: (String, String) -> Unit,
    onReenviarCodigo: (String, String) -> Unit,
    setFocarCodigo: (Boolean) -> Unit,
    setFormEnabled: (Boolean) -> Unit,
    setError: (String) -> Unit
) {
    AuthContainer {
        Text("Aguardando confirmação", textAlign = TextAlign.Center, fontSize = 28.sp)

        Text(
            buildAnnotatedString {
                append("Digite o código enviado para o e-mail ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(Utils.ofuscarEmail(state.email))
                }
            },
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        val focusRequesters =
            List(AppConfig.TOTAL_DIGITOS_CODIGO_RECUPERACAO) { remember { FocusRequester() } }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            state.codigo.forEachIndexed { index, valor ->
                OutlinedTextField(
                    value = valor,
                    onValueChange = {
                        if (it.length <= 1 && it.all { c -> c.isDigit() }) {
                            val novoCodigo = state.codigo.toMutableList()
                                .also { lista -> lista[index] = it }

                            if (it.isNotEmpty() && index < AppConfig.TOTAL_DIGITOS_CODIGO_RECUPERACAO - 1) {
                                focusRequesters[index + 1].requestFocus()
                            }

                            onCodigoChange(novoCodigo)
                            if (novoCodigo.all { it.isNotEmpty() }) {
                                RecaptchaHelper.exec(
                                    before = { setFormEnabled(false) },
                                    after = { setFormEnabled(true) },
                                    onSuccess = { token, siteKey ->
                                        onCodigoChange(novoCodigo)
                                        onChecarCodigo(token, siteKey)
                                    },
                                    onError = { erro ->
                                        setError(erro)
                                    }
                                )
                            }
                        }
                    },
                    singleLine = true,
                    modifier = Modifier
                        .weight(1f)
                        .height(64.dp)
                        .focusRequester(focusRequesters[index])
                        .onPreviewKeyEvent { keyEvent ->
                            if (keyEvent.type == KeyEventType.KeyDown &&
                                keyEvent.key == Key.Backspace &&
                                state.codigo[index].isEmpty() &&
                                index > 0
                            ) {
                                focusRequesters[index - 1].requestFocus()
                                true
                            } else {
                                false
                            }
                        },
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    enabled = state.isFormEnabled
                )
            }
        }

        LaunchedEffect(state.isFocarCodigo) {
            if (state.isFocarCodigo) {
                focusRequesters.first().requestFocus()
                setFocarCodigo(false)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text("Não recebeu o e-mail? ", style = MaterialTheme.typography.bodyMedium)

            if (state.segundosRestantes > 0) {
                Text(
                    text = "Reenviar em ${state.segundosRestantes}s",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Text(
                    text = "Reenviar o e-mail.",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.clickable(
                        enabled = state.isFormEnabled,
                        onClick = {
                            RecaptchaHelper.exec(
                                before = { setFormEnabled(false) },
                                after = { setFormEnabled(true) },
                                onSuccess = { token, siteKey ->
                                    onReenviarCodigo(token, siteKey)
                                },
                                onError = { erro ->
                                    setError(erro)
                                }
                            )
                        }
                    )
                )
            }
        }

        ErrorTextWithFocus(state.error)
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun CriarContaConfirmarCodigoPreview() {
    val previewState = CriarContaUiState(
        email = "fabioedusantos@gmail.com"
    )

    BaseAppTheme (darkTheme = false) {
        CriarContaConfirmarCodigo(
            state = previewState,
            onCodigoChange = {},
            onChecarCodigo = { _, _ -> },
            onReenviarCodigo = { _, _ -> },
            setFocarCodigo = {},
            setFormEnabled = {},
            setError = {}
        )
    }
}