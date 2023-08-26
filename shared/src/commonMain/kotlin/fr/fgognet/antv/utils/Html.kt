package fr.fgognet.antv.utils

import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import io.github.aakira.napier.Napier
import kotlin.math.min

private const val TAG = "ANTV/HTMLUtils"

@OptIn(ExperimentalTextApi::class)
@Composable
fun HtmlText(htmlText: String) {
    val uriHandler = LocalUriHandler.current
    val annotatedString = htmlText.parseHtml()
    ClickableText(text = annotatedString, onClick = { offset ->
        annotatedString.getUrlAnnotations(
            start = offset,
            end = offset
        ).firstOrNull()?.let { stringAnnotation ->
            Napier.v(tag = TAG, message = "opened link " + stringAnnotation.item.url)
            uriHandler.openUri(stringAnnotation.item.url)
        }
    })
}

private val tags = listOf(B, I, U, A)

/**
 * The main entry point. Call this on a String and use the result in a Text.
 */
fun String.parseHtml(): AnnotatedString {
    val newlineReplace = this.replace("<br>", "\n").replace("<br/>", "\n")

    return buildAnnotatedString {
        recurse(newlineReplace, this)
    }
}


/**
 * Recurses through the given HTML String to convert it to an AnnotatedString.
 *
 * @param string the String to examine.
 * @param to the AnnotatedString to append to.
 */
private fun recurse(string: String, to: AnnotatedString.Builder) {
    //Find the opening tag that the given String starts with, if any.
    val startTag = tags.find { string.startsWith(it.startTag()) }

    //Find the closing tag that the given String starts with, if any.
    val endTag = tags.find { string.startsWith(it.endTag()) }

    when {
        //If the String starts with a closing tag, then pop the latest-applied
        //SpanStyle and continue recursing.
        endTag != null -> {
            endTag.endStyle(to)
            recurse(string.removeRange(0, endTag.endTag().length), to)
        }
        //If the String starts with an opening tag, apply the appropriate
        //SpanStyle and continue recursing.
        startTag != null -> {
            startTag.tagBuilder(string, to)
            recurse(string.removeRange(0, startTag.tagOffset(string)), to)
        }
        //If the String doesn't start with an opening or closing tag, but does contain either,
        //find the lowest index (that isn't -1/not found) for either an opening or closing tag.
        //Append the text normally up until that lowest index, and then recurse starting from that index.
        tags.any { string.contains(it.startTag()) || string.contains(it.endTag()) } -> {
            val firstStart =
                tags.map { it.startTag() }.map { string.indexOf(it) }.filterNot { it == -1 }
                    .minOrNull() ?: -1
            val firstEnd =
                tags.map { it.endTag() }.map { string.indexOf(it) }.filterNot { it == -1 }
                    .minOrNull() ?: -1
            val first = when {
                firstStart == -1 -> firstEnd
                firstEnd == -1 -> firstStart
                else -> min(firstStart, firstEnd)
            }

            to.append(string.substring(0, first))

            recurse(string.removeRange(0, first), to)
        }
        //There weren't any supported tags found in the text. Just append it all normally.
        else -> {
            to.append(string)
        }
    }
}


interface Tag {
    val tagName: String
    val popperCount: Int
    fun startTag(): String {
        return "<$tagName"
    }

    fun endTag(): String {
        return "</$tagName>"
    }

    fun tagBuilder(text: String, to: AnnotatedString.Builder)
    fun tagOffset(text: String): Int {
        return text.indexOf('>') + 1
    }

    fun endStyle(to: AnnotatedString.Builder) {
        for (i in 0..<popperCount) {
            to.pop()
        }
    }
}

object B : Tag {
    override val tagName: String
        get() = "b"
    override val popperCount: Int
        get() = 1

    override fun tagBuilder(text: String, to: AnnotatedString.Builder) {
        to.pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
    }

}

object I : Tag {
    override val tagName: String
        get() = "i"
    override val popperCount: Int
        get() = 1

    override fun tagBuilder(text: String, to: AnnotatedString.Builder) {
        to.pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
    }

}

object U : Tag {
    override val tagName: String
        get() = "i"
    override val popperCount: Int
        get() = 1

    override fun tagBuilder(text: String, to: AnnotatedString.Builder) {
        to.pushStyle(SpanStyle(textDecoration = TextDecoration.Underline))
    }

}

object A : Tag {

    private val regex = "<a.+href=\"(.+)\"".toRegex()
    override val tagName: String
        get() = "a"
    override val popperCount: Int
        get() = 2

    @OptIn(ExperimentalTextApi::class)
    override fun tagBuilder(text: String, to: AnnotatedString.Builder) {
        to.pushUrlAnnotation(
            UrlAnnotation(
                url = regex.find(
                    text.subSequence(
                        0,
                        tagOffset(text)
                    )
                )?.groups?.get(1)?.value ?: ""
            )
        )
        to.pushStyle(
            SpanStyle(
                color = Color.Blue,
                textDecoration = TextDecoration.Underline
            )
        )
    }

}