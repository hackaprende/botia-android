package com.hackaprende.botia.customers.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.hackaprende.botia.customers.R
import com.hackaprende.botia.customers.utils.WHATSAPP_URL
import com.hackaprende.botia.ui.ui.theme.BotiaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CustomersActivity : ComponentActivity() {
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
                        CustomersScreen(logoutUser = {
                            openAuthActivity()
                        }, onCustomerSelected = {
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

    private fun openAuthActivity() {
        try {
            startActivity(
                Intent(
                    this,
                    Class.forName("com.hackaprende.botia.auth.ui.AuthActivity")
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

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BotiaTheme {
        Greeting("Android")
    }
}