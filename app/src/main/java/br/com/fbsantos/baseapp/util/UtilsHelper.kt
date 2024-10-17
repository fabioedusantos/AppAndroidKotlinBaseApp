package br.com.fbsantos.baseapp.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay

object UtilsHelper {
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

}