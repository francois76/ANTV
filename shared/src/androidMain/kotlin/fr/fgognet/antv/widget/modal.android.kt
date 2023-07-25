package fr.fgognet.antv.widget

import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat

@Composable
actual fun Modal(
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

@Composable
fun HtmlText(html: String) {
    AndroidView(
        factory = { context ->
            val text = TextView(context)
            text.movementMethod = LinkMovementMethod.getInstance()
            text
        },
        update = { it.text = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_COMPACT) }
    )
}