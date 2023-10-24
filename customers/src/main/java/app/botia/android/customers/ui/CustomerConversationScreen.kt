package app.botia.android.customers.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.botia.android.core.api.ApiResponseStatus
import app.botia.android.customers.R
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
    val messageToSend = remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CustomerConversationScreenTopBar(
                customer = customer,
                onNavigationIconClick = onBackButtonClick
            )
        }
    ) { paddingValues ->
        Column {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    modifier = Modifier
                        .weight(1f),
                    value = messageToSend.value,
                    onValueChange = { newText ->
                        messageToSend.value = newText
                    },
                    placeholder = {
                        Text(text = stringResource(id = R.string.message))
                    }
                )
                IconButton(onClick = {
                    // TODO - Send message
                }) {
                    Icon(
                        imageVector = Icons.Filled.Send,
                        contentDescription = stringResource(id = R.string.send),
                        modifier = Modifier
                            .size(48.dp)
                            .padding(start = 8.dp)
                    )
                }
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
                                MessageText(
                                    modifier = modifier,
                                    textAlignment = Alignment.End,
                                    text = item.customerAnswer,
                                    date = item.lastInteractionDate,
                                    hour = item.lastInteractionHour,
                                )

                                Spacer(
                                    modifier = modifier
                                        .padding(top = 8.dp, bottom = 8.dp)
                                )

                                MessageText(
                                    modifier = modifier,
                                    textAlignment = Alignment.Start,
                                    text = item.userAnswer,
                                    date = item.lastInteractionDate,
                                    hour = item.lastInteractionHour,
                                )
                            }
                        }

                        CustomerMessage.SENDER_CUSTOMER -> {
                            MessageText(
                                modifier = modifier,
                                textAlignment = Alignment.End,
                                text = item.customerAnswer,
                                date = item.lastInteractionDate,
                                hour = item.lastInteractionHour,
                            )
                        }

                        CustomerMessage.SENDER_USER -> {
                            MessageText(
                                modifier = modifier,
                                textAlignment = Alignment.Start,
                                text = item.userAnswer,
                                date = item.lastInteractionDate,
                                hour = item.lastInteractionHour,
                            )
                        }
                    }
                }
            }
        )
    }
}

@Composable
private fun MessageText(
    modifier: Modifier = Modifier,
    textAlignment: Alignment.Horizontal,
    text: String,
    date: String,
    hour: String,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = textAlignment,
    ) {
        Text(text = text)
        Row {
            Text(text = date)
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = hour)
        }
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

