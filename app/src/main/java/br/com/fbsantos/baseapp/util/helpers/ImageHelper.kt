package br.com.fbsantos.baseapp.util.helpers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Base64
import br.com.fbsantos.baseapp.R
import coil.request.ImageRequest
import java.io.ByteArrayOutputStream

/**
 * Helper utilitário para manipulação de imagens (Bitmap) e conversão para Base64.
 *
 * Funcionalidades:
 * - Redimensionamento proporcional de imagens mantendo a proporção original.
 * - Conversão de [Bitmap] para Base64 (formato PNG).
 * - Conversão de [Uri] para Base64 com redimensionamento automático.
 *
 * Útil para cenários onde seja necessário enviar imagens como string
 * (ex.: uploads em APIs REST).
 */
object ImageHelper {
    /**
     * Redimensiona um [Bitmap] proporcionalmente, mantendo a razão entre largura e altura.
     *
     * @param original Bitmap original.
     * @param minSize Menor dimensão (largura ou altura) desejada, em pixels.
     * @return Novo [Bitmap] redimensionado proporcionalmente.
     */
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

    /**
     * Converte um [Bitmap] para string no formato Base64 (prefixado com data:image/png;base64,).
     *
     * @param bitmap Imagem a ser convertida.
     * @return String contendo o bitmap codificado em Base64.
     */
    private fun bitmapToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val byteArray = outputStream.toByteArray()
        val base64 = Base64.encodeToString(byteArray, Base64.NO_WRAP)
        return "data:image/png;base64,$base64"
    }

    /**
     * Converte uma [Uri] de imagem em string Base64, redimensionando proporcionalmente
     * para que a menor dimensão tenha [maxSize] pixels.
     *
     * - Usa [ImageDecoder] em Android P (API 28) ou superior.
     * - Usa [MediaStore] em versões anteriores.
     * - Remove o prefixo `"data:image/png;base64,"` na string final retornada.
     *
     * @param context Contexto usado para acessar o [ContentResolver].
     * @param uri Uri da imagem a ser carregada e convertida.
     * @param maxSize Menor dimensão (largura ou altura) desejada para redimensionamento.
     * @return String Base64 sem o prefixo `"data:image/png;base64,"`, ou `null` em caso de erro.
     */
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

    /**
     * Converte uma string Base64 representando uma imagem em um objeto utilizável pelo Coil ou retorna uma imagem padrão.
     *
     * @param context Contexto da aplicação, usado para construção da requisição de imagem.
     * @param blob String contendo a imagem em formato Base64. Pode conter ou não o prefixo `data:image/...;base64,`.
     * @param defaultDrawable ID do recurso drawable a ser usado como placeholder ou fallback caso a conversão falhe.
     * @return Um [ImageRequest] pronto para uso no Coil ou o ID do drawable padrão se ocorrer erro ou a string for nula/vazia.
     *
     * @see byteArrayToImage Para conversão direta de um array de bytes.
     */
    fun blobBase64ToImage(
        context: Context,
        blob: String?,
        defaultDrawable: Int = R.drawable.placeholder_user
    ): Any {
        if (blob.isNullOrEmpty()) return defaultDrawable

        return try {
            val base64Data = blob.substringAfter("base64,", blob)
            val imageBytes = Base64.decode(base64Data, Base64.DEFAULT)
            return byteArrayToImage(context, imageBytes, defaultDrawable)
        } catch (e: Exception) {
            defaultDrawable
        }
    }

    /**
     * Converte um array de bytes representando uma imagem em um objeto utilizável pelo Coil ou retorna uma imagem padrão.
     *
     * @param context Contexto da aplicação, usado para construção da requisição de imagem.
     * @param imageBytes Array de bytes da imagem. Se for nulo ou vazio, o método retorna o drawable padrão.
     * @param defaultDrawable ID do recurso drawable a ser usado como placeholder ou fallback caso a conversão falhe.
     * @return Um [ImageRequest] configurado para exibir a imagem com efeito de transição ou o ID do drawable padrão se houver falha.
     */
    fun byteArrayToImage(
        context: Context,
        imageBytes: ByteArray?,
        defaultDrawable: Int = R.drawable.placeholder_user
    ): Any {
        if (imageBytes == null || imageBytes.isEmpty()) return defaultDrawable

        return try {
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            ImageRequest.Builder(context)
                .data(bitmap)
                .crossfade(true)
                .placeholder(defaultDrawable)
                .error(defaultDrawable)
                .build()
        } catch (e: Exception) {
            defaultDrawable
        }
    }
}
