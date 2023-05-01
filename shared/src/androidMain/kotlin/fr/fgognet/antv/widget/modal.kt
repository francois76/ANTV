package fr.fgognet.antv.widget

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
actual fun modal(
    title: String,
    content: String,
    confirmButton: String,
    closeCallBack: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            closeCallBack()
        },
        title = {
            Text(title)
        },
        text = {
            HtmlText(
                html = content
            )
        },
        confirmButton = {
            TextButton(
                onClick = {
                    closeCallBack()
                }
            ) {
                Text(confirmButton)
            }
        }
    )
}