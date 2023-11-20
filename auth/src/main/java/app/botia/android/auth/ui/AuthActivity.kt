package app.botia.android.auth.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.material3.Surface
import app.botia.android.auth.R
import app.botia.android.core.api.ApiServiceInterceptorHandler
import app.botia.android.ui.ui.theme.BotiaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BotiaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AuthScreen(onUserLoggedIn = { authenticationToken ->
                        ApiServiceInterceptorHandler.setSessionToken(authenticationToken)
                        openCustomersActivity()
                    })
                }
            }
        }
    }

    private fun openCustomersActivity() {
        try {
            startActivity(
                Intent(
                    this,
                    Class.forName("app.botia.android.customers.ui.CustomersActivity")
                )
            )
        } catch (e: ClassNotFoundException) {
            Toast.makeText(this,
                getString(R.string.customers_activity_not_found), Toast.LENGTH_SHORT).show()
        }
        finish()
    }
}
