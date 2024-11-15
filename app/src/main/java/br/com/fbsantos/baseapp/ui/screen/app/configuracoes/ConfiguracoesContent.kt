package br.com.fbsantos.ui.main.configuracoes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fbsantos.baseapp.config.AppConfig
import br.com.fbsantos.baseapp.config.navigation.Routes
import br.com.fbsantos.baseapp.ui.components.SwitchToggle
import br.com.fbsantos.baseapp.ui.components.container.MainContainer
import br.com.fbsantos.baseapp.ui.theme.BaseAppTheme
import br.com.fbsantos.baseapp.util.BiometricHelper
import br.com.fbsantos.baseapp.util.NavHelper
import br.com.fbsantos.baseapp.util.SnackbarManager
import br.com.fbsantos.baseapp.util.Utils
import br.com.fbsantos.baseapp.util.permissions.PermNotificationHelper
import br.com.fbsantos.ui.app.AppUiState

@Composable
fun ConfiguracoesContent(
    navController: NavController,
    appState: AppUiState,
    onSair: () -> Unit,
    onToggleTheme: (Boolean) -> Unit,
    onToggleBiometria: (Boolean) -> Unit
) {
    val context = LocalContext.current

    MainContainer(
        navController = navController,
        title = "Configurações",
        appState = appState,
        onSair = onSair
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Preferências", style = MaterialTheme.typography.titleMedium)

            // Tema
            SwitchToggle(
                title = "Tema Escuro",
                isChecked = appState.isDarkTheme ?: isSystemInDarkTheme(),
                onCheckedChange = onToggleTheme
            )

            // Notificações
            var isNotificacoesAtivas by remember { mutableStateOf(false) }
            var solicitarPermissao by remember { mutableStateOf(false) }
            isNotificacoesAtivas = PermNotificationHelper.isGranted(context)

            if (solicitarPermissao) {
                PermNotificationHelper.requestPermission(
                    context = context,
                    updateStatus = {
                        isNotificacoesAtivas = it
                        solicitarPermissao = false
                    }
                )
            }
            SwitchToggle(
                title = "Notificações",
                isChecked = isNotificacoesAtivas,
                onCheckedChange = {
                    if (it) { //se for modificado para true
                        solicitarPermissao = true
                    } else {
                        SnackbarManager.show(
                            "Para desativar as notificações, vá até: \n" +
                                    "Configurações > Aplicativos > ${AppConfig.appName} > Notificações"
                        )
                    }
                }
            )

            // Biometria
            SwitchToggle(
                title = "Autenticação do dispositivo",
                isChecked = appState.isDeviceAuthEnabled,
                onCheckedChange = {
                    if (it && !BiometricHelper.isBiometricAvailable(context)) { //se for modificado para true
                        SnackbarManager.show("Nenhum método de desbloqueio configurado. Ative biometria ou bloqueio de tela nas configurações.")
                    } else {
                        onToggleBiometria(it)
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider()
            Text("Sobre o Aplicativo", style = MaterialTheme.typography.titleMedium)

            ListItem(
                headlineContent = { Text("Versão") },
                supportingContent = { Text(Utils.getAppVersion()) },
                trailingContent = { Icon(Icons.Default.Info, contentDescription = null) }
            )

            ListItem(
                headlineContent = { Text("Termos e Condições de Uso") },
                modifier = Modifier.clickable {
                    NavHelper.abrir(
                        navController,
                        Routes.TermosCondicoes.route
                    )
                }
            )

            ListItem(
                headlineContent = { Text("Política de Privacidade") },
                modifier = Modifier.clickable {
                    NavHelper.abrir(
                        navController,
                        Routes.PoliticaPrivacidade.route
                    )
                }
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ConfiguracoesContentPreview() {
    val navController = rememberNavController()

    val appState = AppUiState()

    BaseAppTheme(darkTheme = false) {
        ConfiguracoesContent(
            navController = navController,
            appState = appState,
            onSair = {},
            onToggleTheme = {},
            onToggleBiometria = {}
        )
    }
}