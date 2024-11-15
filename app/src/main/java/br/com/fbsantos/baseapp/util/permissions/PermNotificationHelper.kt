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

object PermNotificationHelper {
    var permissionName = Manifest.permission.POST_NOTIFICATIONS

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

    @Composable
    fun permissionLauncher(callBack: (Boolean) -> Unit): ManagedActivityResultLauncher<String, Boolean> {
        return rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            callBack(isGranted)
        }
    }

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