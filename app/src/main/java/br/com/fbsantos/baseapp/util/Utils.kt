package br.com.fbsantos.baseapp.util

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import br.com.fbsantos.baseapp.R
import kotlinx.coroutines.delay
import android.util.Base64
import coil.request.ImageRequest

object Utils {
    /**
     * Executa uma contagem regressiva e chama uma função a cada segundo.
     *
     * @param segundosRestantes Tempo atual da contagem.
     * @param atualizar Função chamada com o novo valor a cada tick.
     */
    @Composable
    fun contagemRegressiva(
        segundosRestantes: Int,
        atualizar: (Int) -> Unit
    ) {
        LaunchedEffect(key1 = segundosRestantes) {
            if (segundosRestantes > 0) {
                delay(1000L)
                atualizar(segundosRestantes - 1)
            }
        }
    }

    /**
     * Ofusca um endereço de e-mail para exibição segura, preservando parte do nome de usuário
     * e mantendo o domínio visível.
     *
     * O nome antes do símbolo '@' será parcialmente substituído por asteriscos, mantendo
     * os 2 primeiros e o último caractere (se houver) visíveis.
     *
     * Exemplos:
     * - fabioedusantos@gmail.com → fa*******s@gmail.com
     * - joao@gmail.com → jo**@gmail.com
     *
     * @param email O e-mail completo que será ofuscado.
     * @return Uma string com o e-mail ofuscado. Se o e-mail estiver em formato inválido,
     *         será retornado sem alterações.
     */
    fun ofuscarEmail(email: String): String {
        val partes = email.split("@")
        if (partes.size != 2) return email // Retorna como está se o formato não for válido

        val nome = partes[0]
        val dominio = partes[1]

        val visivelInicio = nome.take(2)
        val visivelFim = if (nome.length > 4) nome.takeLast(1) else ""

        val asteriscos = "*".repeat((nome.length - visivelInicio.length - visivelFim.length))

        return "$visivelInicio$asteriscos$visivelFim@$dominio"
    }

    /**
     * Retorna a versão da aplicação (versionName) build.gradle.kts (app) em String
     * Exemplo: 1.0.0
     */
    @Composable
    fun getAppVersion(): String {
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

    /**
     * Separa o primeiro nome e o último sobrenome de um nome completo.
     *
     * Exemplo:
     * - "Fábio Eduardo dos Santos" → ("Fábio", "Santos")
     * - "Carlos" → ("Carlos", null)
     *
     * @param displayName Nome completo do usuário (ex: do Firebase).
     * @return Um [Pair] onde o primeiro valor é o nome e o segundo o sobrenome (ou `null` se não houver).
     */
    fun separarNomeSobrenome(displayName: String?): Pair<String?, String?> {
        if (displayName.isNullOrBlank()) return null to null

        val partes = displayName.trim().split("\\s+".toRegex())
        val nome = partes.firstOrNull().orEmpty()
        val sobrenome = if (partes.size > 1) partes.last() else null

        return nome to sobrenome
    }

    /**
     * Converte uma string Base64 representando uma imagem em um objeto utilizável pelo Coil ou retorna uma imagem padrão.
     *
     * @param context Contexto da aplicação, usado para construção da requisição de imagem.
     * @param fotoBlob String contendo a imagem em formato Base64. Pode conter ou não o prefixo `data:image/...;base64,`.
     * @param defaultDrawable ID do recurso drawable a ser usado como placeholder ou fallback caso a conversão falhe.
     * @return Um [ImageRequest] pronto para uso no Coil ou o ID do drawable padrão se ocorrer erro ou a string for nula/vazia.
     *
     * @see fotoByteArrayToImage Para conversão direta de um array de bytes.
     */
    fun fotoBase64ToImage(
        context: Context,
        fotoBlob: String?,
        defaultDrawable: Int = R.drawable.placeholder_user
    ): Any {
        if (fotoBlob.isNullOrEmpty()) return defaultDrawable

        return try {
            val base64Data = fotoBlob.substringAfter("base64,", fotoBlob)
            val imageBytes = Base64.decode(base64Data, Base64.DEFAULT)
            return fotoByteArrayToImage(context, imageBytes, defaultDrawable)
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
    fun fotoByteArrayToImage(
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

    /**
     * Detecta se a aplicação está no modo preview
     */
    fun isRunningInPreview(): Boolean {
        val vm = System.getProperty("java.vm.name")?.lowercase().orEmpty()
        val tooling = System.getProperty("androidx.compose.ui.tooling") == "true"
        val hasInvoker = try { Class.forName("androidx.compose.ui.tooling.ComposableInvoker"); true } catch (_: Throwable) { false }
        return vm.contains("layoutlib") || tooling || hasInvoker
    }
}