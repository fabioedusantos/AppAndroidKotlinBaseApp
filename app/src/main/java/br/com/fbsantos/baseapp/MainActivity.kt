package br.com.fbsantos.baseapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import br.com.fbsantos.baseapp.config.AppConfig
import br.com.fbsantos.baseapp.config.navigation.Routes
import br.com.fbsantos.baseapp.config.navigation.isValidRoute
import br.com.fbsantos.baseapp.config.navigation.registerAuthScreens
import br.com.fbsantos.baseapp.ui.theme.BaseAppTheme
import br.com.fbsantos.baseapp.util.helpers.AnimatedManager
import br.com.fbsantos.baseapp.util.helpers.SecurityManager
import br.com.fbsantos.baseapp.util.helpers.ToastManager
import br.com.fbsantos.baseapp.config.navigation.registerPrivateScreens
import br.com.fbsantos.baseapp.config.navigation.registerPublicScreens
import br.com.fbsantos.baseapp.ui.AppViewModel
import androidx.navigation.compose.NavHost
import br.com.fbsantos.baseapp.util.helpers.Nav
import org.koin.compose.koinInject

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current.applicationContext

            AppConfig.appName = context.applicationInfo.loadLabel(context.packageManager).toString()

            val appViewModel: AppViewModel = koinInject()
            val appState by appViewModel.uiState.collectAsState()

            if (appState.isReadyDb) {
                val navController = rememberNavController()
                BaseAppTheme(
                    darkTheme = appState.isDarkTheme
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Routes.Splash.route,
                        enterTransition = { AnimatedManager.slideNavTransition()(this) },
                        exitTransition = { AnimatedManager.slideNavExitTransition()(this) },
                        popEnterTransition = { AnimatedManager.slideNavTransition()(this) },
                        popExitTransition = { AnimatedManager.slideNavExitTransition()(this) }
                    ) {
                        registerAuthScreens(navController)
                        registerPublicScreens(navController)
                        registerPrivateScreens(navController)
                    }
                }

                ToastManager.listener()

                // Se veio de push notification, navega após inicializar
                val linkFromNotification = intent.getStringExtra("notification_link")
                LaunchedEffect(linkFromNotification) {
                    if (!linkFromNotification.isNullOrEmpty() && isValidRoute(linkFromNotification)) {
                        SecurityManager.firstRoute = linkFromNotification       //se a aplicação não estiver aberta, setamos como primeira rota
                        Nav.abrir(navController, linkFromNotification)    //se a aplicação estiver aberta, abrirá a url citada
                    }
                }

                SecurityManager.validate(navController)
            }
        }
    }

}