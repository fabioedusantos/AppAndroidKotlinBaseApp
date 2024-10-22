package br.com.fbsantos.baseapp.ui.screen.legal

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fbsantos.baseapp.di.appModule
import br.com.fbsantos.baseapp.ui.components.container.MarkdownContainer
import br.com.fbsantos.baseapp.ui.theme.BaseAppTheme
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@Composable
fun TermosCondicoesScreen(
    navController: NavController
) {
    MarkdownContainer(navController, "Termos e Condições", "termos_condicoes.md")
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun TermosCondicoesScreenPreview() {
    val context = LocalContext.current
    startKoin {
        androidContext(context)
        modules(appModule)
    }

    val navController = rememberNavController()
    BaseAppTheme {
        TermosCondicoesScreen(navController = navController)
    }
}