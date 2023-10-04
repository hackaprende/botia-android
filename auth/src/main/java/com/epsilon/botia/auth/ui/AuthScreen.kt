package com.epsilon.botia.auth.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.epsilon.botia.auth.ui.AuthNavDestinations.LoginScreenDestination
import com.epsilon.botia.auth.ui.AuthNavDestinations.SignUpScreenDestination
import com.epsilon.botia.core.api.ApiResponseStatus
import com.epsilon.botia.ui.ErrorDialog
import com.epsilon.botia.ui.LoadingWheel

@Composable
fun AuthScreen(
    onUserLoggedIn: (String) -> Unit,
    authViewModel: AuthViewModel = hiltViewModel(),
) {
    val state = authViewModel.state.collectAsState().value
    val status = state.status
    val authenticationToken = state.authenticationToken

    if (!authenticationToken.isNullOrEmpty()) {
        onUserLoggedIn(authenticationToken)
    }

    val navController = rememberNavController()

    AuthNavHost(
        navController = navController,
        onLoginButtonClick = { email, password -> authViewModel.login(email, password) },
        onSignUpButtonClick = { email, password, confirmPassword, firstName, lastName ->
            authViewModel.signUp(email, password, confirmPassword, firstName, lastName)
        },
        authViewModel = authViewModel,
    )

    if (status is ApiResponseStatus.Loading) {
        LoadingWheel()
    } else if (status is ApiResponseStatus.Error) {
        ErrorDialog(status.message) { authViewModel.resetApiResponseStatus() }
    }
}

@Composable
private fun AuthNavHost(
    navController: NavHostController,
    onLoginButtonClick: (String, String) -> Unit,
    onSignUpButtonClick: (
        email: String,
        password: String,
        passwordConfirmation: String,
        firstName: String,
        lastName: String
    ) -> Unit,
    authViewModel: AuthViewModel,
) {
    NavHost(
        navController = navController,
        startDestination = LoginScreenDestination
    ) {
        composable(route = LoginScreenDestination) {
            LoginScreen(
                onLoginButtonClick = onLoginButtonClick,
                onRegisterButtonClick = {
                    navController.navigate(route = SignUpScreenDestination)
                },
                authViewModel = authViewModel,
            )
        }

        composable(route = SignUpScreenDestination) {
            SignUpScreen(
                onSignUpButtonClick = onSignUpButtonClick,
                onNavigationIconClick = { navController.navigateUp() },
                authViewModel = authViewModel,
            )
        }
    }
}