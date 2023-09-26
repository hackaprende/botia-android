package com.hackaprende.botia.auth.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.tooling.preview.Preview
import com.hackaprende.botia.ui.ui.theme.BotiaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BotiaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AuthScreen(onUserLoggedIn = {
                        user ->
                        // TODO - Open logged in screen
                        Log.d("MANZANA", "Logged in!!!! ${user.id}\n${user.email}\n${user.authenticationToken}")
                        Toast.makeText(this@LoginActivity, "Logged in!!!!", Toast.LENGTH_SHORT).show()
                    })
                }
            }
        }
    }
}
