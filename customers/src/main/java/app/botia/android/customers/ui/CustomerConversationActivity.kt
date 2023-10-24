package app.botia.android.customers.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import app.botia.android.customers.ui.ui.theme.BotiaTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CustomerConversationActivity : ComponentActivity() {

    companion object {
        const val CUSTOMER_ID_KEY = "customer_id"
        const val COMPANY_ID_KEY = "company_id"

        fun makeIntent(
            context: Context,
            companyId: Int,
            customerId: Int,
        ) = Intent(context, CustomerConversationActivity::class.java).apply {
            putExtra(COMPANY_ID_KEY, companyId)
            putExtra(CUSTOMER_ID_KEY, customerId)
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
                    CustomerConversationScreen(
                        onBackButtonClick = {
                            onBackPressedDispatcher.onBackPressed()
                        }
                    )
                }
            }
        }
    }
}
