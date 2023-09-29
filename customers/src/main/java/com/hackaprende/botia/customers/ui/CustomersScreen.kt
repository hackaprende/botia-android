package com.hackaprende.botia.customers.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.hackaprende.botia.customers.model.Customer


@Composable
fun CustomersScreen(
    logoutUser: () -> Unit,
    onCustomerSelected: (Customer) -> Unit,
    customersViewModel: CustomersViewModel = hiltViewModel()
) {
    val state = customersViewModel.state.collectAsState().value
    val isUserLoggedIn = state.isUserLoggedIn
    val customers = state.customers

    if (!isUserLoggedIn) {
        logoutUser()
    }

    CustomerList(
        customers = customers,
        onCustomerSelected = {
            onCustomerSelected(it)
    })
}

@Composable
private fun CustomerList(
    customers: List<Customer>,
    onCustomerSelected: (Customer) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
    ) {
        itemsIndexed(
            items = customers,
            key = { _, item -> item.hashCode() },
            itemContent = { index, item ->
                Button(onClick = {
                    onCustomerSelected(item)
                }) {
                    Text(item.phoneNumber)
                }
            }
        )
    }
}