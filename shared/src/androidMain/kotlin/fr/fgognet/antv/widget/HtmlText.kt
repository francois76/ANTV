package fr.fgognet.antv.widget

import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat

@Composable
actual fun HtmlText(html: String, modifier: Modifier) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            val text = TextView(context)
            text.movementMethod = LinkMovementMethod.getInstance()
            text
        },
        update = { it.text = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_COMPACT) }
    )
}