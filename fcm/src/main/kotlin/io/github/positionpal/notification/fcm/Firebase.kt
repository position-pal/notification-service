package io.github.positionpal.notification.fcm

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import io.github.positionpal.entities.NotificationMessage
import java.io.FileInputStream
import java.lang.Thread.currentThread

/**
 * A Firebase client facade to send notifications.
 */
class Firebase private constructor(private val app: FirebaseMessaging) {

    /**
     * Configuration for the Firebase client.
     * @property serviceAccountFilePath The path to the service account file.
     */
    data class Configuration(
        val serviceAccountFilePath: String,
    )

    /** Sends the given [notificationMessage] using the specified [token]. */
    fun sendMessage(token: String, notificationMessage: NotificationMessage) = runCatching {
        val notification = Notification.builder()
            .setTitle(notificationMessage.title())
            .build()
        val message = Message.builder()
            .setNotification(notification)
            .putData("title", notificationMessage.title())
            .putData("body", notificationMessage.body())
            .setToken(token)
            .build()
        app.send(message)
    }

    /** A factory for creating instances of [Firebase]. */
    companion object {
        private const val APP_ID = "notification-service"

        /** Creates a new instance of [Firebase] using the given [configuration]. */
        fun create(configuration: Configuration): Result<Firebase> = runCatching {
            val serviceAccountFilePath =
                checkNotNull(currentThread().contextClassLoader.getResource(configuration.serviceAccountFilePath)) {
                    "Unable to access resource ${configuration.serviceAccountFilePath}"
                }.toURI().path
            val credentials = GoogleCredentials.fromStream(FileInputStream(serviceAccountFilePath))
            val options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .build()
            return Result.success(Firebase(FirebaseMessaging.getInstance(FirebaseApp.initializeApp(options, APP_ID))))
        }
    }
}
