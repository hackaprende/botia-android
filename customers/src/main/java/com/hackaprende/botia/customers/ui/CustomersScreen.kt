package com.hackaprende.botia.customers.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun CustomersScreen(
    logoutUser: () -> Unit,
    customersViewModel: CustomersViewModel = hiltViewModel()
) {
    val state = customersViewModel.state.collectAsState().value
    val isUserLoggedIn = state.isUserLoggedIn

    if (!isUserLoggedIn) {
        logoutUser()
    }

}