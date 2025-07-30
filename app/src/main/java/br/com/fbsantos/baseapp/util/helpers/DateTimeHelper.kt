package br.com.fbsantos.baseapp.util.helpers

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Utilitário para conversão entre data e hora (`Date`) e `String` nos formatos padrão.
 *
 * - Formato da API: `"yyyy-MM-dd HH:mm:ss"` (ex: 2025-08-21 14:35:00)
 * - Formato de exibição: `"dd/MM/yyyy HH:mm"` (ex: 21/08/2025 14:35)
 */
object DateTimeHelper {
    private val apiFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
    private val displayFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt", "BR"))
    private val timeFormat = SimpleDateFormat("HH:mm", Locale("pt", "BR"))

    /**
     * Converte uma `String` no formato da API para um objeto `Date`.
     *
     * @param dateTimeString Data e hora no formato `"yyyy-MM-dd HH:mm:ss"`
     * @return Objeto `Date` correspondente ou `null` se o parse falhar
     */
    fun stringToDateTime(dateTimeString: String?): Date? {
        if (dateTimeString.isNullOrEmpty()) return null
        return try {
            apiFormat.parse(dateTimeString)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Converte um objeto `Date` para `String` no formato de exibição.
     *
     * @param date Objeto `Date` a ser formatado
     * @return Data e hora formatadas como `"dd/MM/yyyy HH:mm"`
     */
    fun dateTimeToString(date: Date?): String {
        if (date == null) return "n/a"
        return displayFormat.format(date)
    }

    /**
     * Converte um timestamp (em milissegundos) para um formato de data e hora
     * amigável para o usuário.
     *
     * A formatação segue a lógica:
     * - Menos de 1 minuto → "agora"
     * - Menos de 60 minutos → "X minutos atrás"
     * - Menos de 24 horas no mesmo dia → "Xh atrás"
     * - Exatamente 1 dia atrás → "ontem às HH:mm"
     * - Caso contrário → data completa no formato "dd/MM/yyyy HH:mm"
     *
     * @param timestamp Valor em milissegundos desde 1º de janeiro de 1970 (Epoch time).
     * @return String formatada de forma amigável para exibição ao usuário.
     */
    fun toUserFriendly(timestamp: Long): String {
        val now = Calendar.getInstance()
        val date = Calendar.getInstance().apply { timeInMillis = timestamp }

        val diffMillis = now.timeInMillis - date.timeInMillis
        val minutesDiff = diffMillis / (60 * 1000)
        val hoursDiff = diffMillis / (60 * 60 * 1000)
        val daysDiff = ((now.timeInMillis / 86400000) - (date.timeInMillis / 86400000))

        return when {
            minutesDiff < 1 -> "agora"
            minutesDiff < 60 -> "$minutesDiff minutos atrás"
            hoursDiff < 24 && daysDiff == 0L -> "${hoursDiff}h atrás"
            daysDiff == 1L -> "ontem às ${timeFormat.format(date.time)}"
            else -> displayFormat.format(date.time)
        }
    }

    /**
     * Converte um objeto [Date] para um formato de data e hora
     * amigável para o usuário.
     *
     * Internamente, delega para [toUserFriendly] usando o timestamp da data.
     *
     * @param date Objeto [Date] que será formatado.
     * @return String formatada de forma amigável para exibição ao usuário.
     */
    fun toUserFriendly(date: Date): String {
        return toUserFriendly(date.time)
    }
}