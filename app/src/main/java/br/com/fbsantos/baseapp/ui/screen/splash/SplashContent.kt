package br.com.fbsantos.baseapp.ui.screen.splash

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.fbsantos.baseapp.R
import br.com.fbsantos.baseapp.ui.theme.BaseAppTheme
import br.com.fbsantos.baseapp.ui.theme.onBackgroundLight
import br.com.fbsantos.baseapp.ui.theme.primaryLight
import br.com.fbsantos.baseapp.util.helpers.BiometricManager
import br.com.fbsantos.baseapp.util.helpers.ToastManager
import br.com.fbsantos.ui.app.AppUiState
import kotlinx.coroutines.delay

@Composable
fun SplashContent(
    appState: AppUiState,
    onLogin: () -> Unit,
    onSair: () -> Unit
) {
    val context = LocalContext.current
    var alphaTarget by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        alphaTarget = 1f
        delay(2000)

        val isDeviceAuthEnabled = appState.isDeviceAuthEnabled
        if (!appState.isLoggedIn
            && isDeviceAuthEnabled && BiometricManager.isAvailable(context)
        ) {
            BiometricManager.showPrompt(
                context,
                onSuccess = {
                    onLogin()
                },
                onError = { mensagemErro ->
                    ToastManager.show(mensagemErro)
                    onSair()
                }
            )
        } else {
            onLogin()
        }
    }

    val alpha by animateFloatAsState(
        targetValue = alphaTarget,
        animationSpec = tween(durationMillis = 1200),
        label = "LogoFadeIn"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(primaryLight, onBackgroundLight),
                    center = Offset.Infinite,
                    radius = 1000f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            modifier = Modifier
                .size(160.dp)
                .graphicsLayer(alpha = alpha)
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SplashContentPreview() {
    val previewAppState = AppUiState(
        isDeviceAuthEnabled = true
    )

    BaseAppTheme(darkTheme = true) {
        SplashContent(
            appState = previewAppState,
            onLogin = {},
            onSair = {}
        )
    }
}