package br.com.fbsantos.baseapp.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import com.google.firebase.messaging.FirebaseMessaging

data class NotificationTopic(
    val id: String,
    val name: String,
)

object FirebaseNotificationHelper {
    private const val TAG = "FIREBASE-PUSH"

    private val allTopics = listOf(
        NotificationTopic("gerais", "Notificações Gerais"),
        NotificationTopic("promocoes", "Promoções"),
        NotificationTopic("atualizacoes", "Atualizações")
    )

    private fun clearAllNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(context, NotificationManager::class.java)!!
            val channels = manager.notificationChannels
            channels.forEach { channel ->
                manager.deleteNotificationChannel(channel.id)
                Log.d(TAG, "✅ Removeu o tópico '${channel.id}'")
            }
        }
    }

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

            Log.d(TAG, "✅ Criou o tópico '$channelId' '$channelName'")
        }
    }

    fun subscribeToAll(context: Context, onResult: ((Boolean) -> Unit)? = null) {
        var successCount = 0
        clearAllNotificationChannel(context)
        allTopics.forEach { topic ->
            createNotificationChannel(
                context = context,
                channelId = topic.id,
                channelName = topic.name
            )

            FirebaseMessaging.getInstance().subscribeToTopic(topic.id)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "✅ Inscrito no tópico '${topic.id}'")
                        successCount++
                    } else {
                        Log.e(TAG, "❌ Falha ao inscrever no tópico '${topic.id}'", task.exception)
                    }
                    if (successCount == allTopics.size) onResult?.invoke(true)
                }
        }
    }

    fun unsubscribeFromAll(onResult: ((Boolean) -> Unit)? = null) {
        var successCount = 0
        allTopics.forEach { topic ->
            FirebaseMessaging.getInstance().unsubscribeFromTopic(topic.id)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "🚫 Cancelou inscrição no tópico '${topic.id}'")
                        successCount++
                    } else {
                        Log.e(
                            TAG,
                            "❌ Falha ao cancelar inscrição no tópico '${topic.id}'",
                            task.exception
                        )
                    }
                    if (successCount == allTopics.size) onResult?.invoke(true)
                }
        }
    }
}