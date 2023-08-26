package fr.fgognet.antv.utils

import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import kotlin.math.min

/**
 * The tags to interpret. Add tags here and in [tagToStyle].
 */
private val tags = listOf(B, I, U)

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
        tags.any { string.startsWith(it.endTag()) } -> {
            to.pop()
            recurse(string.removeRange(0, endTag!!.endTag().length), to)
        }
        //If the String starts with an opening tag, apply the appropriate
        //SpanStyle and continue recursing.
        tags.any { string.startsWith(it.startTag()) } -> {
            startTag?.tagBuilder(to)
            recurse(string.removeRange(0, startTag?.tagOffset()!!), to)
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
    fun startTag(): String {
        return "<$tagName"
    }

    fun endTag(): String {
        return "</$tagName>"
    }

    fun tagBuilder(to: AnnotatedString.Builder)
    fun tagOffset(): Int
}

object B : Tag {
    override val tagName: String
        get() = "b"

    override fun tagBuilder(to: AnnotatedString.Builder) {
        to.pushStyle(SpanStyle(fontWeight = FontWeight.Bold))
    }

    override fun tagOffset(): Int {
        return 3
    }
}

object I : Tag {
    override val tagName: String
        get() = "i"

    override fun tagBuilder(to: AnnotatedString.Builder) {
        to.pushStyle(SpanStyle(fontStyle = FontStyle.Italic))
    }

    override fun tagOffset(): Int {
        return 3
    }
}

object U : Tag {
    override val tagName: String
        get() = "i"

    override fun tagBuilder(to: AnnotatedString.Builder) {
        to.pushStyle(SpanStyle(textDecoration = TextDecoration.Underline))
    }

    override fun tagOffset(): Int {
        return 3
    }
}