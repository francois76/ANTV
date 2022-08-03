package fr.fgognet.antv.utils

import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.style.URLSpan
import android.text.util.Linkify


fun linkifyHtml(html: String, linkifyMask: Int): Spannable {
    val text = Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
    val currentSpans = text.getSpans(0, text.length, URLSpan::class.java)
    val buffer = SpannableString(text)
    Linkify.addLinks(buffer, linkifyMask)
    for (span in currentSpans) {
        val end = text.getSpanEnd(span)
        val start = text.getSpanStart(span)
        buffer.setSpan(span, start, end, 0)
    }
    return buffer
}