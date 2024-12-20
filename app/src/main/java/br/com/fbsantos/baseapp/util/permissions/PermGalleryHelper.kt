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

object PermGalleryHelper {
    val permissionName: String

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionName = Manifest.permission.READ_MEDIA_IMAGES
        } else {
            permissionName = Manifest.permission.READ_EXTERNAL_STORAGE
        }
    }

    fun isGranted(context: Context): Boolean {
        return (ContextCompat.checkSelfPermission(context, permissionName)
                == PackageManager.PERMISSION_GRANTED)
    }

    @Composable
    fun galleryLauncher(callBack: (Uri?) -> Unit): ManagedActivityResultLauncher<Intent, ActivityResult> {
        return rememberLauncherForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            callBack(result.data?.data)
        }
    }

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

    fun createIntent(): Intent {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        return intent
    }
}