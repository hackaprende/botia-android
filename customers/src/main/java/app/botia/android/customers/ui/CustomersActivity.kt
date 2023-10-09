package app.botia.android.customers.ui

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import app.botia.android.customers.R
import app.botia.android.customers.utils.WHATSAPP_URL
import app.botia.android.ui.ui.theme.BotiaTheme
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CustomersActivity : ComponentActivity() {

    private val tag = CustomersActivity::class.java.simpleName

    // Launcher to request permission to show notification
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications. No need to do anything here.
            retrieveFirebaseNotificationsToken()
        } else {
            // Inform users they need notifications to get app full potential.
            showNotificationsDisabledDialog()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BotiaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        CustomersScreen(
                            logoutUser = {
                                openAuthActivity()
                            },
                            setupFirebaseNotifications = ::setupFirebaseNotifications,
                            onCustomerSelected = {
                                val url = "$WHATSAPP_URL${it.phoneNumber}"
                                val i = Intent(Intent.ACTION_VIEW)
                                i.data = Uri.parse(url)
                                startActivity(i)
                            }
                        )
                    }
                }
            }
        }
    }

    private fun setupFirebaseNotifications() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                retrieveFirebaseNotificationsToken()
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                showPermissionRationaleNotificationDialog()
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            // If build version < Tiramisu there is no need to ask for permission
            retrieveFirebaseNotificationsToken()
        }
    }

    private fun retrieveFirebaseNotificationsToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(tag, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            val msg = "FCM Token added $token"
            Log.d(tag, msg)
            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })
    }

    private fun showPermissionRationaleNotificationDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.notifications_permission_dialog_title))
            .setMessage(getString(R.string.notifications_permission_dialog_message))
            .setPositiveButton(
                getString(R.string.allow)
            ) { _, _ ->
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
            .setNegativeButton(R.string.no, null)
            .show()
    }

    private fun showNotificationsDisabledDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.app_not_showing_notifications_dialog_title))
            .setMessage(getString(R.string.app_not_showing_notifications_dialog_message))
            .setPositiveButton(
                getString(R.string.allow)
            ) { _, _ ->
                var settingsIntent: Intent? = null
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    settingsIntent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                }
                startActivity(settingsIntent)
            }
            .setNegativeButton(R.string.no, null)
            .show()
    }


    private fun openAuthActivity() {
        try {
            startActivity(
                Intent(
                    this,
                    Class.forName("app.botia.android.auth.ui.AuthActivity")
                )
            )
        } catch (e: ClassNotFoundException) {
            Toast.makeText(
                this,
                getString(R.string.login_activity_not_found), Toast.LENGTH_SHORT
            ).show()
        }
        finish()
    }
}
