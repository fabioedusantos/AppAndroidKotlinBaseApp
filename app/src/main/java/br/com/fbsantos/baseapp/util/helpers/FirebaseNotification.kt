package br.com.fbsantos.baseapp.util.helpers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import br.com.fbsantos.baseapp.config.AppConfig
import com.google.firebase.messaging.FirebaseMessaging

/**
 * Helper para gerenciar notificações do Firebase Cloud Messaging (FCM)
 * e canais de notificação no Android.
 *
 * Funcionalidades:
 * - Gerenciar e criar canais de notificação no Android 8.0+.
 * - Inscrever o app em múltiplos tópicos FCM.
 * - Cancelar inscrição de todos os tópicos configurados.
 *
 * Observações:
 * - Os canais são recriados sempre que ocorre uma nova inscrição.
 * - As ações são logadas em modo debug ([AppConfig.IS_DEBUG]).
 */
object FirebaseNotification {
    private const val TAG = "FIREBASE-PUSH"

    val allTopics = listOf(
        "gerais" to "Notificações Gerais",
        "promocoes" to "Promoções",
        "atualizacoes" to "Atualizações"
    )

    /**
     * Remove todos os canais de notificação existentes no dispositivo.
     *
     * Requer Android 8.0 (API 26) ou superior.
     *
     * @param context Contexto usado para acessar o [NotificationManager].
     */
    private fun clearAllNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(context, NotificationManager::class.java)!!
            val channels = manager.notificationChannels
            channels.forEach { channel ->
                manager.deleteNotificationChannel(channel.id)
                if (AppConfig.IS_DEBUG) Log.d(TAG, "✅ Removeu o tópico '${channel.id}'")
            }
        }
    }

    /**
     * Cria um canal de notificação para um tópico específico.
     *
     * Requer Android 8.0 (API 26) ou superior.
     *
     * @param context Contexto usado para criar o canal.
     * @param channelId ID do canal (geralmente o mesmo ID do tópico FCM).
     * @param channelName Nome amigável do canal exibido ao usuário.
     */
    private fun createNotificationChannel(
        context: Context,
        channelId: String,
        channelName: String
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            channel.description = channelName

            val notificationManager = getSystemService(context, NotificationManager::class.java)!!
            notificationManager.createNotificationChannel(channel)

            if (AppConfig.IS_DEBUG) Log.d(TAG, "✅ Criou o tópico '$channelId' '$channelName'")
        }
    }

    /**
     * Inscreve o dispositivo em **todos** os tópicos definidos em [allTopics].
     *
     * Passos:
     * 1. Remove todos os canais antigos ([clearAllNotificationChannel]).
     * 2. Cria novos canais para cada tópico ([createNotificationChannel]).
     * 3. Inscreve no FCM usando [FirebaseMessaging.subscribeToTopic].
     *
     * @param context Contexto usado para gerenciar canais.
     * @param onResult Callback opcional chamado quando todas as inscrições
     *                 forem processadas. `true` significa que todos os tópicos
     *                 foram inscritos com sucesso.
     */
    fun subscribeToAll(context: Context, onResult: ((Boolean) -> Unit)? = null) {
        var successCount = 0
        clearAllNotificationChannel(context)
        allTopics.forEach { topic ->
            createNotificationChannel(
                context = context,
                channelId = topic.first,
                channelName = topic.second
            )

            FirebaseMessaging.getInstance().subscribeToTopic(topic.first)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (AppConfig.IS_DEBUG) Log.d(TAG, "✅ Inscrito no tópico '${topic.first}'")
                        successCount++
                    } else {
                        if (AppConfig.IS_DEBUG) Log.e(TAG, "❌ Falha ao inscrever no tópico '${topic.first}'", task.exception)
                    }
                    if (successCount == allTopics.size) onResult?.invoke(true)
                }
        }
    }

    /**
     * Cancela a inscrição do dispositivo em **todos** os tópicos definidos em [allTopics].
     *
     * @param onResult Callback opcional chamado quando todas as desinscrições forem processadas.
     *                 `true` significa que todas as inscrições foram canceladas com sucesso.
     */
    fun unsubscribeFromAll(onResult: ((Boolean) -> Unit)? = null) {
        var successCount = 0
        allTopics.forEach { topic ->
            FirebaseMessaging.getInstance().unsubscribeFromTopic(topic.first)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (AppConfig.IS_DEBUG) Log.d(TAG, "🚫 Cancelou inscrição no tópico '${topic.first}'")
                        successCount++
                    } else {
                        if (AppConfig.IS_DEBUG) Log.e(
                            TAG,
                            "❌ Falha ao cancelar inscrição no tópico '${topic.first}'",
                            task.exception
                        )
                    }
                    if (successCount == allTopics.size) onResult?.invoke(true)
                }
        }
    }
}