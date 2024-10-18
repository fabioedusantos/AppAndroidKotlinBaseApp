package br.com.fbsantos.baseapp.ui.screen.auth.criarconta.content

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fbsantos.baseapp.config.navigation.Routes
import br.com.fbsantos.baseapp.ui.components.container.AuthContainer
import br.com.fbsantos.baseapp.ui.theme.BaseAppTheme
import br.com.fbsantos.baseapp.util.NavHelper
import br.com.fbsantos.baseapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

@Composable
fun CriarContaSucesso(navController: NavController) {
    AuthContainer {
        Text("Conta criada com sucesso!", textAlign = TextAlign.Center, fontSize = 28.sp)

        Text(
            text = "Agora é só voltar e fazer login. Boa sorte nessa nova jornada!",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Button(
            onClick = { NavHelper.abrir(navController, Routes.Login.route, Routes.CriarConta.route) },
            shape = RoundedCornerShape(50),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Fazer login agora")
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

    BaseAppTheme (darkTheme = false) {
        CriarContaSucesso(
            navController = navController
        )
    }
}