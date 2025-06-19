package com.matiasdelbel.mvi.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.matiasdelbel.mvi.ui.theme.AppTheme
import com.matiasdelbel.mvi.ui.theme.spacers

@Composable
fun DecoratedTextField(
    label: @Composable () -> Unit,
    textField: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    helperText: (@Composable () -> Unit)? = null,
    error: (@Composable () -> Unit)? = null,
) {
    Column(modifier = modifier) {
        label()
        Spacer(Modifier.height(height = MaterialTheme.spacers.sm))
        textField()
        if (isError) {
            error?.let { it() }
        }
        helperText?.let { it() }
    }
}

@Preview(showBackground = true)
@Composable
internal fun DecoratedTextFieldPreview() {
    AppTheme {
        Column {
            DecoratedTextField(
                isError = false,
                label = { TextFieldLabel(text = "Label") },
                textField = {
                    TextField(
                        value = "",
                        onValueChange = { },
                        isError = false,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                helperText = { TextFieldHelperText(text = "Helper text") },
            )

            DecoratedTextField(
                isError = true,
                label = { TextFieldLabel(text = "Label") },
                textField = {
                    TextField(
                        value = "",
                        onValueChange = { },
                        isError = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                helperText = { TextFieldHelperText(text = "Helper text") },
                error = { TextFieldError(text = "Error") }
            )
        }
    }
}