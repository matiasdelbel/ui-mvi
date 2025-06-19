package com.matiasdelbel.mvi.ui.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.matiasdelbel.mvi.ui.theme.AppTheme

@Composable
fun TextFieldHint(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
internal fun TextFieldHintPreview() {
    AppTheme {
        TextFieldHint(text = "TextField hint")
    }
}
