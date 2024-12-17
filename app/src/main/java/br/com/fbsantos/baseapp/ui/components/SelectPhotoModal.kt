package br.com.fbsantos.baseapp.ui.components

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import br.com.fbsantos.baseapp.util.ToastManager
import br.com.fbsantos.baseapp.util.permissions.PermCameraHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectPhotoModal(
    context: Context,
    modalTitle: String = "Escolha uma foto",
    onDismiss: () -> Unit,
    onImageSelected: (Uri?) -> Unit
) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = PermCameraHelper.cameraLauncher { isSuccess ->
        onImageSelected(if (isSuccess) imageUri else null)
        onDismiss()
    }

    val cameraPermissionLauncher = PermCameraHelper.permissionLauncher(context) { isGranted, uri ->
        if (isGranted) {
            imageUri = uri
            imageUri?.let { cameraLauncher.launch(it) }
        } else {
            ToastManager.show("Permissão da câmera negada.")
        }
    }

    ModalBottomSheet(onDismissRequest = onDismiss) {
        Column(Modifier.padding(vertical = 8.dp)) {
            Text(
                text = modalTitle,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            photoOptionItem("Usar Câmera", Icons.Default.PhotoCamera) {
                if (PermCameraHelper.isGranted(context)) {
                    imageUri = PermCameraHelper.createUri(context)
                    imageUri?.let { cameraLauncher.launch(it) }
                } else {
                    cameraPermissionLauncher.launch(PermCameraHelper.permissionName)
                }
            }

            photoOptionItem("Escolher da Galeria", Icons.Default.Image) {
                //todo escolher foto da galeria
            }

            photoOptionItem("Remover Foto", Icons.Default.Delete) {
                //todo remover foto
            }
        }
    }
}


@Composable
private fun photoOptionItem(text: String, icon: ImageVector, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text(text) },
        leadingContent = { Icon(icon, contentDescription = null) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .clickable { onClick() },
        colors = ListItemDefaults.colors(
            containerColor = Color.Transparent
        )
    )
}
