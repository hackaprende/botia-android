package app.botia.android.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import app.botia.android.R
import app.botia.android.core.NOTIFICATION_ACTION_CUSTOMER_NEED_HELP
import app.botia.android.core.NOTIFICATION_ACTION_KEY
import app.botia.android.core.NOTIFICATION_CUSTOMER_ID_KEY
import app.botia.android.core.NOTIFICATION_CUSTOMER_PHONE_KEY
import app.botia.android.core.util.SessionManager
import app.botia.android.customers.ui.CustomersActivity
import app.botia.android.workers.FirebaseNotificationsWorker
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FirebaseMessagingService : FirebaseMessagingService() {

    private enum class NotificationType {
        CUSTOMER_NEED_HELP, DEFAULT
    }

    companion object {

        private val TAG = FirebaseMessagingService::class.java.simpleName
    }

    @Inject
    lateinit var sessionManager: SessionManager

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        val data = remoteMessage.data
        if (data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: $data")

            // Check if data needs to be processed by long running job
            if (isLongRunningJob()) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                scheduleJob()
            } else {
                // Handle message within 10 seconds
                handleNow()
            }
        }

        var notificationType = NotificationType.DEFAULT
        var customerPhone: String? = null
        var customerId: String? = null
        var action: String? = null
        if (data.containsKey(NOTIFICATION_ACTION_KEY)) {
            action = data["action"]
            if (action == NOTIFICATION_ACTION_CUSTOMER_NEED_HELP) {
                customerPhone = data[NOTIFICATION_CUSTOMER_PHONE_KEY]
                customerId = data[NOTIFICATION_CUSTOMER_ID_KEY]
                notificationType = NotificationType.CUSTOMER_NEED_HELP
            }
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
            it.body?.let { body ->
                it.title?.let { title ->
                    sendNotification(
                        title,
                        body,
                        notificationType,
                        action,
                        customerPhone,
                        customerId,
                    )
                }
            }
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    private fun isLongRunningJob() = true

    // [START on_new_token]
    /**
     * Called if the FCM registration token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the
     * FCM registration token is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        val job = Job()
        val scope = CoroutineScope(Dispatchers.IO + job)
        scope.launch {
            sessionManager.storeFcmToken(token)
        }
    }
    // [END on_new_token]

    /**
     * Schedule async work using WorkManager.
     */
    private fun scheduleJob() {
        // [START dispatch_job]
        val work = OneTimeWorkRequest.Builder(FirebaseNotificationsWorker::class.java).build()
        WorkManager.getInstance(this).beginWith(work).enqueue()
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private fun handleNow() {
        Log.d(TAG, "Short lived task is done.")
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private fun sendNotification(
        messageTitle: String,
        messageBody: String,
        notificationType: NotificationType = NotificationType.DEFAULT,
        action: String? = null,
        customerPhone: String? = null,
        customerId: String? = null
    ) {
        val requestCode = 0
        val intent = Intent(this, CustomersActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        if (action != null) {
            intent.putExtra(NOTIFICATION_ACTION_KEY, action)
            if (notificationType == NotificationType.CUSTOMER_NEED_HELP) {
                intent.putExtra(NOTIFICATION_CUSTOMER_PHONE_KEY, customerPhone)
                intent.putExtra(NOTIFICATION_CUSTOMER_ID_KEY, customerId)
            }
        }

        val pendingIntent = PendingIntent.getActivity(
            this,
            requestCode,
            intent,
            PendingIntent.FLAG_IMMUTABLE,
        )

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(messageTitle)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_HIGH,
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationId = 0
        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}