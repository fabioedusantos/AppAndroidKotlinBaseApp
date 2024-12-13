package br.com.fbsantos.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import java.io.ByteArrayOutputStream

object ImageHelper {

    private fun resizeBitmapProportional(original: Bitmap, minSize: Int = 96): Bitmap {
        val width = original.width
        val height = original.height

        val scale = if (width < height) {
            minSize.toFloat() / width
        } else {
            minSize.toFloat() / height
        }

        val newWidth = (width * scale).toInt()
        val newHeight = (height * scale).toInt()

        return Bitmap.createScaledBitmap(original, newWidth, newHeight, true)
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val byteArray = outputStream.toByteArray()
        val base64 = Base64.encodeToString(byteArray, Base64.NO_WRAP)
        return "data:image/png;base64,$base64"
    }

    fun uriToBase64(context: Context, uri: Uri, maxSize: Int = 96): String? {
        return try {
            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            } else {
                @Suppress("DEPRECATION")
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            }

            val resized = resizeBitmapProportional(bitmap, maxSize)
            val base64 = bitmapToBase64(resized)
            base64.substringAfter("base64,", base64) //removemos o data:image/png;base64,
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
