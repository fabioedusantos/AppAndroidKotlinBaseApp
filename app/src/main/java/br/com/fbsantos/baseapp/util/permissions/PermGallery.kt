package br.com.fbsantos.baseapp.util.permissions

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat

/**
 * Utilitário para gerenciamento de permissão de galeria e seleção de imagens
 * com integração a Jetpack Compose.
 *
 * Ele lida automaticamente com as diferenças de permissão entre versões do Android:
 * - Android 13+ (API 33+): [Manifest.permission.READ_MEDIA_IMAGES]
 * - Android 12-: [Manifest.permission.READ_EXTERNAL_STORAGE]
 */
object PermGallery {
    /** Nome da permissão de leitura de imagens, definido conforme a versão do Android. */
    val permissionName: String

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionName = Manifest.permission.READ_MEDIA_IMAGES
        } else {
            permissionName = Manifest.permission.READ_EXTERNAL_STORAGE
        }
    }

    /**
     * Verifica se a permissão para acesso à galeria foi concedida.
     *
     * @param context Contexto atual.
     * @return `true` se concedida, `false` caso contrário.
     */
    fun isGranted(context: Context): Boolean {
        return (ContextCompat.checkSelfPermission(context, permissionName)
                == PackageManager.PERMISSION_GRANTED)
    }

    /**
     * Launcher para abrir a galeria e obter o [Uri] da imagem selecionada.
     *
     * @param callBack Retorna o URI da imagem ou `null` se cancelado.
     */
    @Composable
    fun galleryLauncher(callBack: (Uri?) -> Unit): ManagedActivityResultLauncher<Intent, ActivityResult> {
        return rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            callBack(result.data?.data)
        }
    }

    /**
     * Launcher para solicitar permissão de leitura de imagens.
     *
     * Ao conceder, já retorna um [Intent] configurado para abrir a galeria.
     *
     * @param callBack Recebe:
     * - `Boolean`: indica se a permissão foi concedida.
     * - `Intent?`: intent para seleção de imagem, ou `null` se a permissão foi negada.
     */
    @Composable
    fun permissionLauncher(callBack: (Boolean, Intent?) -> Unit): ManagedActivityResultLauncher<String, Boolean> {
        return rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            var intent: Intent? = null
            if (isGranted) {
                intent = createIntent()
            }
            callBack(isGranted, intent)
        }
    }

    /**
     * Cria um intent para abrir a galeria na aba de imagens.
     *
     * @return [Intent] configurado para seleção de imagens.
     */
    fun createIntent(): Intent {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        return intent
    }
}