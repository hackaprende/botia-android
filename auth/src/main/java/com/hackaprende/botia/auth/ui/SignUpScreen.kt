package com.hackaprende.botia.auth.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.hackaprende.botia.auth.R
import com.hackaprende.botia.ui.AuthField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    onSignUpButtonClick: (email: String, password: String, passwordConfirmation: String) -> Unit,
    onNavigationIconClick: () -> Unit,
    authViewModel: AuthViewModel,
) {
    Scaffold(
        topBar = {
            SignUpScreenToolbar {
                onNavigationIconClick()
                authViewModel.resetFieldErrors()
            }
        }
    ) {
        Content(
            modifier = Modifier.padding(it),
            resetFieldErrors = { authViewModel.resetFieldErrors() },
            onSignUpButtonClick = onSignUpButtonClick,
            authViewModel = authViewModel,
        )
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    resetFieldErrors: () -> Unit,
    onSignUpButtonClick: (email: String, password: String, passwordConfirmation: String) -> Unit,
    authViewModel: AuthViewModel,
) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val confirmPassword = remember { mutableStateOf("") }
    val state = authViewModel.state.collectAsState().value

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = 32.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = 16.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AuthField(
            label = stringResource(id = R.string.email),
            modifier = Modifier
                .fillMaxWidth(),
            email = email.value,
            onTextChanged = {
                email.value = it
                resetFieldErrors()
            },
            errorMessageId = state.emailFieldError
        )

        AuthField(
            label = stringResource(id = R.string.password),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            email = password.value,
            onTextChanged = {
                password.value = it
                resetFieldErrors()
            },
            visualTransformation = PasswordVisualTransformation(),
            errorMessageId = state.passwordFieldError,
        )

        AuthField(
            label = stringResource(id = R.string.confirm_password),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            email = confirmPassword.value,
            onTextChanged = {
                confirmPassword.value = it
                resetFieldErrors()
            },
            visualTransformation = PasswordVisualTransformation(),
            errorMessageId = state.confirmPasswordFieldError,
        )

        Button(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .semantics { testTag = "sign-up-button" },
            onClick = {
                onSignUpButtonClick(email.value, password.value, confirmPassword.value)
            }) {
            Text(
                stringResource(R.string.sign_up),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SignUpScreenToolbar(
    onNavigationIconClick: () -> Unit
) {
    TopAppBar(
        title = { Text(stringResource(com.hackaprende.botia.core.R.string.app_name)) },
        navigationIcon = { BackNavigationIcon { onNavigationIconClick() } }
    )
}

@Composable
fun BackNavigationIcon(
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            painter = rememberVectorPainter(image = Icons.Sharp.ArrowBack),
            contentDescription = null
        )
    }
}

