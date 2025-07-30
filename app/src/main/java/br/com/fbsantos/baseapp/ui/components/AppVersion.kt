package br.com.fbsantos.baseapp.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext


/**
 * Retorna a versão da aplicação (versionName) build.gradle.kts (app) em String
 * Exemplo: 1.0.0
 */
@Composable
fun AppVersion(): String {
    val context = LocalContext.current
    val packageManager = context.packageManager
    val packageName = context.packageName
    return try {
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        packageInfo.versionName ?: "Desconhecida"
    } catch (e: Exception) {
        "Erro ao obter versão da aplicação"
    }
}