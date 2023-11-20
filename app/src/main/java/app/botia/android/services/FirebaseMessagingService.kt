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
import app.botia.android.auth.ui.AuthActivity
import app.botia.android.core.NOTIFICATION_TYPE_CUSTOMER_MESSAGE
import app.botia.android.core.NOTIFICATION_TYPE_KEY
import app.botia.android.core.NOTIFICATION_BODY
import app.botia.android.core.NOTIFICATION_COMPANY_ID_KEY
import app.botia.android.core.NOTIFICATION_CUSTOMER_ID_KEY
import app.botia.android.core.NOTIFICATION_TITLE
import app.botia.android.core.api.ApiServiceInterceptorHandler
import app.botia.android.core.util.SessionManager
import app.botia.android.customers.ui.CustomersActivity
import app.botia.android.workers.FirebaseNotificationsWorker
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FirebaseMessagingService : FirebaseMessagingService() {
    companion object {

        private val TAG = FirebaseMessagingService::class.java.simpleName
    }

    private var job = Job()
    private val backgroundScope = CoroutineScope(Dispatchers.IO + job)

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

        setSessionTokenIfLoggedIn {
            userLoggedIn ->
            val notificationTitle = data[NOTIFICATION_TITLE]
            val notificationBody = data[NOTIFICATION_BODY]
            val intent = buildIntent(userLoggedIn, data)
            notificationTitle?.let { body ->
                notificationBody?.let { title ->
                    sendNotification(
                        title,
                        body,
                        intent,
                    )
                }
            }
        }
    }

    private fun setSessionTokenIfLoggedIn(onSessionTokenSet: (userLoggedIn: Boolean) -> Unit) {
        backgroundScope.launch {
            sessionManager
                .userTokenFlow()
                .collectLatest { authenticationToken ->
                    val userLoggedIn = authenticationToken.isNotEmpty()

                    if (userLoggedIn) {
                        // Set the token to the interceptor so it's added in the headers
                        // of any request that needs authentication.
                        ApiServiceInterceptorHandler.setSessionToken(authenticationToken)
                    }

                    onSessionTokenSet(userLoggedIn)
                }
        }
    }

    private fun buildIntent(userLoggedIn: Boolean, data: Map<String, String>): Intent {
        if (!userLoggedIn) return Intent(this, AuthActivity::class.java)

        if (data.containsKey(NOTIFICATION_TYPE_KEY)) {
            val action = data[NOTIFICATION_TYPE_KEY]
            val companyId = data[NOTIFICATION_COMPANY_ID_KEY]
            val customerId = data[NOTIFICATION_CUSTOMER_ID_KEY]
            if (action == NOTIFICATION_TYPE_CUSTOMER_MESSAGE &&
                companyId != null &&
                customerId != null
            ) {
                return CustomersActivity.makeGoToConversationIntent(
                    context = this,
                    companyId = companyId.toInt(),
                    customerId = customerId.toInt(),
                )
            }
        }

        return Intent(this, CustomersActivity::class.java)
    }

    private fun isLongRunningJob() = false

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
        intent: Intent,
    ) {
        val requestCode = 0

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