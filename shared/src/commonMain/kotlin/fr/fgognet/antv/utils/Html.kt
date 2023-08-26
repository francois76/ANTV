package fr.fgognet.antv.utils

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import fr.fgognet.antv.widget.*
import io.github.aakira.napier.Napier
import kotlin.math.min

private const val TAG = "ANTV/HTMLUtils"

@OptIn(ExperimentalTextApi::class)
@Composable
fun HtmlText(htmlText: String, colorScheme: ColorScheme) {
    val uriHandler = LocalUriHandler.current
    val annotatedString = buildAnnotatedString {
        pushStyle(SpanStyle(color = colorScheme.onSurfaceVariant))
        recurse(colorScheme, htmlText.replace("<br>", "\n").replace("<br/>", "\n"), this)
        pop()
    }
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
 * Recurses through the given HTML String to convert it to an AnnotatedString.
 *
 * @param string the String to examine.
 * @param to the AnnotatedString to append to.
 */
private fun recurse(colorScheme: ColorScheme, string: String, to: AnnotatedString.Builder) {
    //Find the opening tag that the given String starts with, if any.
    val startTag = tags.find { string.startsWith(it.startTag()) }

    //Find the closing tag that the given String starts with, if any.
    val endTag = tags.find { string.startsWith(it.endTag()) }

    when {
        //If the String starts with a closing tag, then pop the latest-applied
        //SpanStyle and continue recursing.
        endTag != null -> {
            endTag.endStyle(to)
            recurse(colorScheme, string.removeRange(0, endTag.endTag().length), to)
        }
        //If the String starts with an opening tag, apply the appropriate
        //SpanStyle and continue recursing.
        startTag != null -> {
            startTag.tagBuilder(string, to, colorScheme)
            recurse(colorScheme, string.removeRange(0, startTag.tagOffset(string)), to)
        }
        //If the String doesn't start with an opening or closing tag, but does contain either,
        //find the lowest index (that isn't -1/not found) for either an opening or closing tag.
        //Append the text normally up until that lowest index, and then recurse starting from that index.
        tags.any { string.contains(it.startTag()) || string.contains(it.endTag()) } -> {
            val firstStart =
                tags.map { string.indexOf(it.startTag()) }.filterNot { it == -1 }
                    .minOrNull() ?: -1
            val firstEnd =
                tags.map { string.indexOf(it.endTag()) }.filterNot { it == -1 }
                    .minOrNull() ?: -1
            val first = when {
                firstStart == -1 -> firstEnd
                firstEnd == -1 -> firstStart
                else -> min(firstStart, firstEnd)
            }

            to.append(string.substring(0, first))

            recurse(colorScheme, string.removeRange(0, first), to)
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

    fun tagBuilder(text: String, to: AnnotatedString.Builder, colorScheme: ColorScheme)
    fun tagOffset(text: String): Int {
        return text.indexOf('>') + 1
    }

    fun endStyle(to: AnnotatedString.Builder) {
        for (i in 0..<popperCount) {
            to.pop()
        }
    }
}

class Property(private val regex: Regex) {
    fun find(text: String): String {
        return this.regex.find(
            text.subSequence(
                0,
                text.indexOf('>') + 1
            )
        )?.groups?.get(1)?.value ?: ""
    }
};

interface TagWithProperties : Tag {
    val properties: HashMap<String, Property>
    fun HashMap<String, Property>.registerProperty(property: String): HashMap<String, Property> {
        this[property] = Property("<$tagName.+$property=\"(.+)\"".toRegex())
        return this
    }
}

object B : Tag {
    override val tagName: String
        get() = "b"
    override val popperCount: Int
        get() = 1

    override fun tagBuilder(text: String, to: AnnotatedString.Builder, colorScheme: ColorScheme) {
        to.pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
    }

}

object I : Tag {
    override val tagName: String
        get() = "i"
    override val popperCount: Int
        get() = 1

    override fun tagBuilder(text: String, to: AnnotatedString.Builder, colorScheme: ColorScheme) {
        to.pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
    }

}

object U : Tag {
    override val tagName: String
        get() = "i"
    override val popperCount: Int
        get() = 1

    override fun tagBuilder(text: String, to: AnnotatedString.Builder, colorScheme: ColorScheme) {
        to.pushStyle(SpanStyle(textDecoration = TextDecoration.Underline))
    }

}

object A : TagWithProperties {

    override val properties: HashMap<String, Property>
        get() = HashMap<String, Property>().registerProperty("href")

    override val tagName: String
        get() = "a"
    override val popperCount: Int
        get() = 2

    @OptIn(ExperimentalTextApi::class)
    override fun tagBuilder(text: String, to: AnnotatedString.Builder, colorScheme: ColorScheme) {
        to.pushUrlAnnotation(
            UrlAnnotation(
                url = properties["href"]?.find(text) ?: "error"
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