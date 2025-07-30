package br.com.fbsantos.baseapp.util.permissions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File

/**
 * Utilitário para gerenciar permissões e captura de imagem pela câmera usando Jetpack Compose.
 *
 * Fornece launchers para solicitar a permissão de câmera, iniciar a captura de foto
 * e criar URIs seguras para salvar imagens temporárias.
 */
object PermCamera {
    /** Nome da permissão de câmera (por padrão [Manifest.permission.CAMERA]). */
    var permissionName = Manifest.permission.CAMERA

    /**
     * Verifica se a permissão de câmera já foi concedida.
     *
     * @param context Contexto atual da aplicação.
     * @return `true` se a permissão já foi concedida, `false` caso contrário.
     */
    fun isGranted(context: Context): Boolean {
        return (ContextCompat.checkSelfPermission(context, permissionName)
                == PackageManager.PERMISSION_GRANTED)
    }

    /**
     * Cria um launcher para iniciar a câmera e capturar uma foto para o URI especificado.
     *
     * Esse launcher deve receber previamente um [Uri] apontando para um arquivo válido
     * (por exemplo, criado com [createUri]).
     *
     * @param callBack Função chamada ao finalizar a captura, recebendo `true` em caso de sucesso.
     */
    @Composable
    fun cameraLauncher(callBack: (Boolean) -> Unit): ManagedActivityResultLauncher<Uri, Boolean> {
        return rememberLauncherForActivityResult(
            ActivityResultContracts.TakePicture()
        ) { success ->
            callBack(success)
        }
    }

    /**
     * Cria um launcher para solicitar a permissão da câmera.
     *
     * Ao ser concedida, já retorna um [Uri] válido para uso imediato na captura.
     *
     * @param context Contexto atual da aplicação.
     * @param callBack Função chamada após o resultado da solicitação, recebendo:
     * - `Boolean`: se a permissão foi concedida.
     * - `Uri?`: URI da imagem, caso concedida; `null` caso negada.
     */
    @Composable
    fun permissionLauncher(
        context: Context,
        callBack: (Boolean, Uri?) -> Unit
    ): ManagedActivityResultLauncher<String, Boolean> {
        return rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            var imageUri: Uri? = null
            if (isGranted) {
                imageUri = createUri(context)
            }
            callBack(isGranted, imageUri)
        }
    }

    /**
     * Cria um arquivo temporário no cache da aplicação e retorna um [Uri] seguro
     * via [FileProvider], para ser usado na captura de imagens.
     *
     * **Importante:** Certifique-se de declarar o provider no `AndroidManifest.xml`:
     * ```xml
     * <provider
     *     android:name="androidx.core.content.FileProvider"
     *     android:authorities="${applicationId}.provider"
     *     android:exported="false"
     *     android:grantUriPermissions="true">
     *     <meta-data
     *         android:name="android.support.FILE_PROVIDER_PATHS"
     *         android:resource="@xml/file_paths" />
     * </provider>
     * ```
     *
     * E configure o `res/xml/file_paths.xml`:
     * ```xml
     * <paths xmlns:android="http://schemas.android.com/apk/res/android">
     *     <cache-path name="cache" path="."/>
     * </paths>
     * ```
     *
     * @param context Contexto da aplicação.
     * @return URI seguro para salvar a imagem.
     */
    fun createUri(context: Context): Uri {
        val file = File.createTempFile("photo_", ".jpg", context.cacheDir)
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
    }
}