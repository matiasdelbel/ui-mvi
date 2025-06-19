package com.matiasdelbel.mvi.ui.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.matiasdelbel.mvi.ui.theme.AppTheme

@Composable
fun TextFieldLabel(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
internal fun TextFieldPreview() {
    AppTheme {
        TextFieldLabel(text = "TextField label")
    }
}
