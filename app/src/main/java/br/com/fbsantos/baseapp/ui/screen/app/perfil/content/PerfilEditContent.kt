package br.com.fbsantos.baseapp.ui.screen.app.perfil.content

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fbsantos.baseapp.ui.components.container.MainContainer
import br.com.fbsantos.baseapp.ui.theme.BaseAppTheme
import br.com.fbsantos.ui.app.AppUiState

@Composable
fun PerfilEditContent(
    navController: NavController,
    appState: AppUiState,
    onSair: () -> Unit
) {
    MainContainer(
        navController = navController,
        title = "Perfil",
        appState = appState,
        onSair = onSair
    ) {

    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PerfilEditContentPreview() {
    val navController = rememberNavController()

    val appState = AppUiState(
        nome = "Fábio",
        sobrenome = "Santos",
        email = "fabioedusantos@gmail.com",
        isContaGoogle = false
    )

    BaseAppTheme (darkTheme = false) {
        PerfilEditContent(
            navController = navController,
            appState = appState,
            onSair = {}
        )
    }
}