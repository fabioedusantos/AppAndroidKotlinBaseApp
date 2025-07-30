package br.com.fbsantos.baseapp.util.helpers

import android.content.Context
import android.graphics.BitmapFactory
import br.com.fbsantos.baseapp.R
import android.util.Base64
import coil.request.ImageRequest

object Utils {

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
     * Detecta se a aplicação está no modo preview
     */
    fun isRunningInPreview(): Boolean {
        val vm = System.getProperty("java.vm.name")?.lowercase().orEmpty()
        val tooling = System.getProperty("androidx.compose.ui.tooling") == "true"
        val hasInvoker = try { Class.forName("androidx.compose.ui.tooling.ComposableInvoker"); true } catch (_: Throwable) { false }
        return vm.contains("layoutlib") || tooling || hasInvoker
    }
}