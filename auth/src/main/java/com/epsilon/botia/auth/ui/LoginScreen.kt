package com.epsilon.botia.auth.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.epsilon.botia.auth.R
import com.epsilon.botia.ui.AuthField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginButtonClick: (String, String) -> Unit,
    onRegisterButtonClick: () -> Unit,
    authViewModel: AuthViewModel,
) {
    Scaffold(
        topBar = { LoginScreenToolbar() }
    ) {
        Content(
            modifier = Modifier.padding(it),
            onLoginButtonClick = onLoginButtonClick,
            onRegisterButtonClick = {
                onRegisterButtonClick()
                authViewModel.resetFieldErrors()
            },
            resetFieldErrors = {
                authViewModel.resetFieldErrors()
            },
            authViewModel = authViewModel
        )
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    onLoginButtonClick: (String, String) -> Unit,
    onRegisterButtonClick: () -> Unit,
    resetFieldErrors: () -> Unit,
    authViewModel: AuthViewModel,
) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

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
            text = email.value,
            onTextChanged = {
                email.value = it
                resetFieldErrors()
            },
            errorMessageId = state.emailFieldError,
            errorSemantic = "email-field-error",
            fieldSemantic = "email-field",
        )

        AuthField(
            label = stringResource(id = R.string.password),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            text = password.value,
            onTextChanged = {
                password.value = it
                resetFieldErrors()
            },
            visualTransformation = PasswordVisualTransformation(),
            errorMessageId = state.passwordFieldError,
            errorSemantic = "password-field-error",
            fieldSemantic = "password-field",
        )

        Button(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
            .semantics { testTag = "login-button" },
            onClick = { onLoginButtonClick(email.value, password.value) }) {
            Text(
                stringResource(R.string.login),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )
        }

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center,
            text = stringResource(R.string.do_not_have_an_account)
        )

        Text(
            modifier = Modifier
                .clickable(enabled = true, onClick = { onRegisterButtonClick() })
                .fillMaxWidth()
                .padding(16.dp)
                .semantics { testTag = "login-screen-register-button" },
            text = stringResource(R.string.register),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreenToolbar() {
    TopAppBar(
        title = { Text(stringResource(com.epsilon.botia.core.R.string.app_name)) },
    )
}