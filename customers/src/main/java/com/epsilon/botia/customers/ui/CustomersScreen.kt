package com.epsilon.botia.customers.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.epsilon.botia.core.api.ApiResponseStatus
import com.epsilon.botia.customers.R
import com.epsilon.botia.customers.model.Customer
import com.epsilon.botia.ui.ErrorDialog
import com.epsilon.botia.ui.LoadingWheel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomersScreen(
    logoutUser: () -> Unit,
    onCustomerSelected: (Customer) -> Unit,
    customersViewModel: CustomersViewModel = hiltViewModel()
) {
    val state = customersViewModel.state.collectAsState().value
    val isUserLoggedIn = state.isUserLoggedIn
    val customers = state.customers
    val status = state.status
    val customerToBeDisabled = remember { mutableStateOf<Customer?>(null) }

    if (!isUserLoggedIn) {
        logoutUser()
    }

    Scaffold(
        topBar = {
            CustomersScreenTopBar(
                onLogoutClick = {
                    customersViewModel.logout()
                }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            CustomerList(
                modifier = Modifier.padding(it),
                customers = customers,
                onEnableBotSwitchClick = {
                        customer ->
                    if (customer.isBotEnabled) {
                        customerToBeDisabled.value = customer
                    } else {
                        customersViewModel.toggleBotEnabledForCustomer(customer)
                    }
                },
                onCustomerSelected = { customer ->
                    customersViewModel.toggleNeedCustomAttentionForCustomer(customer)
                    onCustomerSelected(customer)
                },
            )

            if (status is ApiResponseStatus.Loading) {
                LoadingWheel()
            } else if (status is ApiResponseStatus.Error) {
                ErrorDialog(
                    message = status.message,
                    onDialogDismiss = {
                        customersViewModel.resetApiResponseStatus()
                    },
                )
            }

            val customerToBeDisabledValue = customerToBeDisabled.value
            if (customerToBeDisabledValue != null) {
                DisableBotDialog(
                    onAccept = {
                        customersViewModel.toggleBotEnabledForCustomer(customerToBeDisabledValue)
                        customerToBeDisabled.value = null
                    },
                    onCancel = {
                        customerToBeDisabled.value = null
                    }
                )
            }
        }
    }
}

@Composable
fun DisableBotDialog(
    onAccept: () -> Unit,
    onCancel: () -> Unit,
) {
    AlertDialog(
        modifier = Modifier
            .semantics { testTag = "error-dialog" },
        onDismissRequest = { },
        title = {
            Text(stringResource(R.string.bot_will_be_disabled_alert_title))
        },
        text = {
            Text(stringResource(id = R.string.bot_will_be_disabled_alert_message))
        },
        confirmButton = {
            Button(onClick = { onAccept() }) {
                Text(stringResource(android.R.string.ok))
            }
        },
        dismissButton = {
            Button(onClick = { onCancel() }) {
                Text(stringResource(android.R.string.cancel))
            }
        }
    )
}

@Composable
private fun CustomerList(
    modifier: Modifier = Modifier,
    customers: List<Customer>,
    onEnableBotSwitchClick: (Customer) -> Unit,
    onCustomerSelected: (Customer) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .padding(16.dp),
    ) {
        itemsIndexed(
            items = customers,
            key = { _, item -> item.hashCode() },
            itemContent = { _, item ->

                Row(
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 16.dp)
                        .fillMaxWidth()
                        .background(
                            color = if (item.needCustomAttention) {
                                Color.Cyan
                            } else {
                                Color.White
                            }
                        )
                        .clickable {
                            onCustomerSelected(item)
                        },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Switch(
                        modifier = Modifier
                            .padding(8.dp),
                        checked = item.isBotEnabled,
                        onCheckedChange = {
                            onEnableBotSwitchClick(item)
                        }
                    )

                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = AnnotatedString(text = item.phoneNumber),
                            color = Color.Black,
                            style = TextStyle(
                                fontSize = 16.sp
                            )
                        )

                        Column(
                            horizontalAlignment = Alignment.End,
                        ) {
                            Text(
                                text = item.lastInteractionDate,
                                textAlign = TextAlign.Right,
                                color = Color.Black
                            )
                            Text(
                                modifier = Modifier
                                    .padding(top = 4.dp),
                                text = item.lastInteractionHour,
                                textAlign = TextAlign.Right,
                                color = Color.Black
                            )
                        }
                    }
                }

                Divider(color = Color.Black, thickness = 1.dp)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomersScreenTopBar(onLogoutClick: () -> Unit) {
    var showMenu by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text(stringResource(id = R.string.my_customers)) },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = Color.White,
            titleContentColor = Color.Black
        ),
        actions = {
            IconButton(onClick = { showMenu = !showMenu }) {
                Icon(Icons.Default.MoreVert, stringResource(id = R.string.logout))
            }
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    text = {
                        Text(stringResource(id = R.string.logout))
                    },
                    onClick = onLogoutClick
                )
            }
        }
    )
}