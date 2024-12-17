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

object PermCameraHelper {
    var permissionName = Manifest.permission.CAMERA

    fun isGranted(context: Context): Boolean {
        return (ContextCompat.checkSelfPermission(context, permissionName)
                == PackageManager.PERMISSION_GRANTED)
    }

    @Composable
    fun cameraLauncher(callBack: (Boolean) -> Unit): ManagedActivityResultLauncher<Uri, Boolean> {
        return rememberLauncherForActivityResult(
            ActivityResultContracts.TakePicture()
        ) { success ->
            callBack(success)
        }
    }

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

    fun createUri(context: Context): Uri {
        val file = File.createTempFile("photo_", ".jpg", context.cacheDir)
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            file
        )
    }
}