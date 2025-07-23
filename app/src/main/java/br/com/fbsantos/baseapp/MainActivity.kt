package br.com.fbsantos.baseapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalAnimationApi
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
import br.com.fbsantos.baseapp.util.AnimatedHelper
import br.com.fbsantos.baseapp.util.SecurityManager
import br.com.fbsantos.baseapp.util.ToastManager
import br.com.fbsantos.baseapp.config.navigation.registerPrivateScreens
import br.com.fbsantos.baseapp.config.navigation.registerPublicScreens
import br.com.fbsantos.baseapp.ui.AppViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import org.koin.compose.koinInject

class MainActivity : FragmentActivity() {

    @OptIn(ExperimentalAnimationApi::class)
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
                    //deprecado mas ainda é a melhor opção para efeitos
                    AnimatedNavHost(
                        navController = navController,
                        startDestination = Routes.Splash.route,
                        enterTransition = { AnimatedHelper.slideNavTransition()(this) },
                        exitTransition = { AnimatedHelper.slideNavExitTransition()(this) },
                        popEnterTransition = { AnimatedHelper.slideNavTransition()(this) },
                        popExitTransition = { AnimatedHelper.slideNavExitTransition()(this) }
                    ) {
                        registerAuthScreens(navController)
                        registerPublicScreens(navController)
                        registerPrivateScreens(navController)
                    }
                }

                //Toast default
                val toastError by ToastManager.toastMessage.collectAsState()
                LaunchedEffect(toastError) {
                    toastError?.let {
                        Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                        ToastManager.clear()
                    }
                }
                //Toast default

                // Se veio de push notification, navega após inicializar
                val linkFromNotification = intent.getStringExtra("notification_link")
                LaunchedEffect(linkFromNotification) {
                    if (!linkFromNotification.isNullOrEmpty() && isValidRoute(linkFromNotification)) {
                        navController.navigate(linkFromNotification)
                    }
                }

                SecurityManager.validate(navController)
            }
        }
    }

}