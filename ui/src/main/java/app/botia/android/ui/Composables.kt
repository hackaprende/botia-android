package app.botia.android.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun LoadingWheel() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .semantics { testTag = "loading-wheel" },
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = Color.Red
        )
    }
}

@Composable
fun ErrorDialog(
    message: String,
    onDialogDismiss: () -> Unit,
) {
    AlertDialog(
        modifier = Modifier
            .semantics { testTag = "error-dialog" },
        onDismissRequest = { },
        title = {
            Text(stringResource(R.string.error_dialog_title))
        },
        text = {
            Text(message)
        },
        confirmButton = {
            Button(onClick = { onDialogDismiss() }) {
                Text(stringResource(R.string.try_again))
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthField(
    label: String,
    text: String,
    onTextChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    errorSemantic: String = "",
    fieldSemantic: String = "",
    errorMessageId: Int? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    Column(modifier = modifier,) {
        if (errorMessageId != null) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { testTag = errorSemantic },
                text = stringResource(id = errorMessageId)
            )
        }
        TextField(
            modifier = Modifier.fillMaxWidth()
                .semantics { testTag = fieldSemantic },
            label = { Text(label) },
            value = text,
            onValueChange = { onTextChanged(it) },
            visualTransformation = visualTransformation,
            isError = errorMessageId != null
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun WhiteTopAppBar(toolbarText: String, onNavigationIconClick: () -> Unit) {
    TopAppBar(
        title = { Text(toolbarText) },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = Color.White,
            titleContentColor = Color.Black
        ),
        navigationIcon = {
            BackNavigationIcon(
                contentDescription = stringResource(id = R.string.back),
                onClick = onNavigationIconClick
            )
        },
    )
}

@Composable
private fun BackNavigationIcon(
    contentDescription: String,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick, modifier = Modifier
        .semantics { testTag = "back-navigation-icon" }) {
        Icon(
            painter = rememberVectorPainter(image = Icons.Sharp.ArrowBack),
            contentDescription = contentDescription
        )
    }
}
