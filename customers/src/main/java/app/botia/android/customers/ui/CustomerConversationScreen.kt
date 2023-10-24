package app.botia.android.customers.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.botia.android.core.api.ApiResponseStatus
import app.botia.android.customers.model.Customer
import app.botia.android.customers.model.CustomerMessage
import app.botia.android.ui.ErrorDialog
import app.botia.android.ui.LoadingWheel
import app.botia.android.ui.WhiteTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerConversationScreen(
    onBackButtonClick: () -> Unit,
    customerConversationViewModel: CustomerConversationViewModel = hiltViewModel()
) {

    val state = customerConversationViewModel.state.collectAsState().value
    val status = state.status
    val customer = state.customer
    val customerMessages = customer?.messages ?: listOf()

    Scaffold(
        topBar = {
            CustomerConversationScreenTopBar(
                customer = customer,
                onNavigationIconClick = onBackButtonClick
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            CustomerMessageList(
                customerMessages = customerMessages
            )

            if (status is ApiResponseStatus.Loading) {
                LoadingWheel()
            } else if (status is ApiResponseStatus.Error) {
                ErrorDialog(
                    message = status.message,
                    onDialogDismiss = {
                        customerConversationViewModel.resetApiResponseStatus()
                    },
                )
            }
        }
    }
}

@Composable
fun CustomerMessageList(
    modifier: Modifier = Modifier,
    customerMessages: List<CustomerMessage>,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
    ) {
        itemsIndexed(
            items = customerMessages,
            key = { _, item -> item.hashCode() },
            itemContent = { _, item ->
                Row(
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 8.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    when (item.sender) {
                        CustomerMessage.SENDER_CUSTOMER_BOT -> {
                            Column(
                                modifier = modifier
                                .fillMaxWidth()
                            ) {
                                CustomerMessageText(modifier, item.customerAnswer)

                                Spacer(
                                    modifier = modifier
                                        .padding(top = 8.dp, bottom = 8.dp)
                                )

                                Text(
                                    modifier = modifier
                                        .fillMaxWidth(),
                                    text = item.userAnswer,
                                    textAlign = TextAlign.Left
                                )
                            }
                        }

                        CustomerMessage.SENDER_CUSTOMER -> {
                            CustomerMessageText(modifier, item.customerAnswer)
                        }

                        CustomerMessage.SENDER_USER -> {
                            Text(text = item.userAnswer)
                        }
                    }
                }
            }
        )
    }
}

@Composable
private fun CustomerMessageText(
    modifier: Modifier = Modifier,
    text: String,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        Text(text = text)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerConversationScreenTopBar(
    customer: Customer?,
    onNavigationIconClick: () -> Unit
) {
    val customerName = customer?.name ?: ""
    val toolbarText = if (customerName.isNullOrEmpty()) {
        customer?.phoneNumber ?: ""
    } else {
        customerName
    }
    WhiteTopAppBar(toolbarText, onNavigationIconClick)
}

