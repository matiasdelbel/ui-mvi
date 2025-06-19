package com.matiasdelbel.mvi.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.matiasdelbel.mvi.ui.theme.AppTheme
import com.matiasdelbel.mvi.ui.theme.spacers

@Composable
fun TextFieldError(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.labelSmall,
        modifier = modifier.padding(top = MaterialTheme.spacers.xs)
    )
}

@Preview(showBackground = true)
@Composable
internal fun TextFieldErrorPreview() {
    AppTheme {
        TextFieldError(text = "TextField error")
    }
}
