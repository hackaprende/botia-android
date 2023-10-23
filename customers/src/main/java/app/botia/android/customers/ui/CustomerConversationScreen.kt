package app.botia.android.customers.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun CustomerConversationScreen(
    customerConversationViewModel: CustomerConversationViewModel = hiltViewModel()
) {

    val state = customerConversationViewModel.state.collectAsState().value
    Text(text = state.customer?.phoneNumber ?: "No number")
}
