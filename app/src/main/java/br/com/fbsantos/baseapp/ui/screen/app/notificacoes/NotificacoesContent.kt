package br.com.fbsantos.baseapp.ui.screen.app.notificacoes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fbsantos.baseapp.ui.components.ErrorTextWithFocus
import br.com.fbsantos.baseapp.ui.components.container.MainContainer
import br.com.fbsantos.baseapp.ui.theme.BaseAppTheme
import br.com.fbsantos.baseapp.util.SnackbarManager
import br.com.fbsantos.ui.app.AppUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificacoesContent(
    navController: NavController,
    appState: AppUiState,
    onSair: () -> Unit,
    state: NotificacoesUiState,
    onCanalChange: (String) -> Unit,
    onTituloChange: (String) -> Unit,
    onBodyChange: (String) -> Unit,
    onLinkChange: (String) -> Unit,
    onSalvar: (() -> Unit) -> Unit
) {
    MainContainer(
        navController = navController,
        title = "Notificações",
        appState = appState,
        onSair = onSair,
        fixedBottomContent = {
            Button(
                onClick = {
                    onSalvar({
                        SnackbarManager.show("Notificação enviada com sucesso!")
                    })
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = state.isFormEnabled
            ) {
                Text("Salvar")
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            val channelOptions = listOf(
                "gerais" to "Notificações Gerais",
                "promocoes" to "Promoções",
                "atualizacoes" to "Atualizações"
            )

            var expanded by remember { mutableStateOf(false) }

            // Canal
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .menuAnchor(
                            type = MenuAnchorType.PrimaryNotEditable,
                            enabled = true
                        )
                        .fillMaxWidth(),
                    value = channelOptions.firstOrNull { it.first == state.channelId }?.second.orEmpty(),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Canal") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                    isError = state.isChannelIdError,
                    supportingText = {
                        if (state.isChannelIdError) Text(state.channelIdErrorText)
                    },
                    enabled = state.isFormEnabled
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    channelOptions.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option.second) },
                            onClick = {
                                onCanalChange(option.first)
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))

            // Título
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.title,
                onValueChange = onTituloChange,
                label = { Text("Título") },
                singleLine = true,
                isError = state.isTitleError,
                supportingText = {
                    if (state.isTitleError) Text(state.titleErrorText)
                },
                enabled = state.isFormEnabled
            )

            Spacer(Modifier.height(12.dp))

            // Corpo
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                value = state.body,
                onValueChange = onBodyChange,
                label = { Text("Corpo") },
                singleLine = false,
                minLines = 6,
                maxLines = 10,
                isError = state.isBodyError,
                supportingText = {
                    if (state.isBodyError) Text(state.bodyErrorText)
                },
                enabled = state.isFormEnabled
            )

            Spacer(Modifier.height(12.dp))

            // Link
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.link,
                onValueChange = onLinkChange,
                label = { Text("Link") },
                singleLine = true,
                isError = state.isLinkError,
                supportingText = {
                    if (state.isLinkError) Text(state.linkErrorText)
                },
                enabled = state.isFormEnabled
            )

            ErrorTextWithFocus(state.error)
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun NotificacoesContentPreview() {
    val navController = rememberNavController()

    BaseAppTheme(darkTheme = false) {
        NotificacoesContent(
            navController = navController,
            appState = AppUiState(),
            onSair = {},
            state = NotificacoesUiState(
                isFormEnabled = true
            ),
            onCanalChange = {},
            onTituloChange = {},
            onBodyChange = {},
            onLinkChange = {},
            onSalvar = {}
        )
    }
}