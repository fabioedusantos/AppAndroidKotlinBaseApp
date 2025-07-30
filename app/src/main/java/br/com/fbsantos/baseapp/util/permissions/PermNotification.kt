package br.com.fbsantos.baseapp.util.permissions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat

/**
 * Utilitário para gerenciamento da permissão de notificações no Android 13+.
 *
 * - Em versões anteriores ao Android 13 (API < 33), a permissão [Manifest.permission.POST_NOTIFICATIONS]
 *   não é necessária, retornando sempre `true` em [isGranted].
 * - Em Android 13+, solicita e verifica a permissão conforme requerido pelo sistema.
 */
object PermNotification {

    /** Nome da permissão para postar notificações (Android 13+). */
    var permissionName = Manifest.permission.POST_NOTIFICATIONS

    /**
     * Verifica se a permissão de notificações está concedida.
     *
     * Em versões abaixo do Android 13, retorna sempre `true`.
     *
     * @param context Contexto atual.
     * @return `true` se concedida ou versão < 33, `false` caso contrário.
     */
    fun isGranted(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(
                context,
                permissionName
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            return true
        }
    }

    /**
     * Cria um launcher para solicitar a permissão de notificações.
     *
     * @param callBack Função de retorno com `true` se a permissão foi concedida, `false` caso contrário.
     */
    @Composable
    fun permissionLauncher(callBack: (Boolean) -> Unit): ManagedActivityResultLauncher<String, Boolean> {
        return rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            callBack(isGranted)
        }
    }

    /**
     * Solicita a permissão de notificações automaticamente ao ser chamado.
     *
     * Caso já esteja concedida, atualiza o status diretamente.
     * Caso contrário, dispara o fluxo de solicitação.
     *
     * @param context Contexto atual.
     * @param updateStatus Callback chamado com o status final (`true` se concedida).
     */
    @Composable
    fun requestPermission(context: Context, updateStatus: (Boolean) -> Unit) {
        val permissionLauncher = permissionLauncher{ isGranted ->
            updateStatus(isGranted)
        }

        LaunchedEffect(Unit) {
            if (isGranted(context)) {
                updateStatus(true)
            } else {
                permissionLauncher.launch(permissionName)
            }
        }
    }
}