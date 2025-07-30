package br.com.fbsantos.baseapp

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import br.com.fbsantos.baseapp.config.AppConfig
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        if (AppConfig.IS_DEBUG) Log.d("FCM", "Novo token: $token")
        // Pode enviar para o backend se quiser armazenar também
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        val channelId = remoteMessage.data["channelId"] ?: "gerais"
        val title = remoteMessage.data["title"] ?: "Notificação"
        val body = remoteMessage.data["body"] ?: ""
        val link = remoteMessage.data["link"]

        showNotification(channelId, title, body, link)
    }

    private fun showNotification(channelId: String, title: String, body: String, link: String? = null) {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Sempre abre o app, mas com o link no extra
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            if (!link.isNullOrEmpty()) {
                putExtra("notification_link", link)
            }
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // trocar por ícone próprio depois
            .setContentTitle(title)
            .setContentText(body)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        manager.notify(System.currentTimeMillis().toInt(), notification)
    }

}