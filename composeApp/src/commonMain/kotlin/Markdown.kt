import MarkupElement.*
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

private val markupCharacters = listOf('*', '_', '`')

fun String.processAnnotations(): AnnotatedString {
    return this.processAllAnnotations()
}

private fun String.processAllAnnotations() = buildAnnotatedString {
    val string = this@processAllAnnotations

    val queue = mutableListOf<MarkupElement>()

    var index = 0

    while (index < string.length) {
        val character = string[index]

        if (character !in markupCharacters) {
            append(character)
            index++
            continue
        }

        val element = when (character) {
            '*' -> if (index + 1 < string.length - 1 && string[index + 1] == '*') BOLD else ITALIC_1

            '_' -> ITALIC_2

            '`' -> if (index + 1 < string.length - 1 && string[index + 1] == '`'
                && index + 2 < string.length - 1 && string[index + 2] == '`'
            ) {
                CODE_BLOCK
            } else {
                CODE
            }

            // TODO: consider case where string ends with a markup character
            else -> throw IllegalArgumentException("Unexpected character: $character")
        }

        if (queue.contains(element)) {
            val first = queue.indexOf(element)
            queue.removeAt(first)

            pop()

            if (element == BOLD) {
                index++
            } else if (element == CODE_BLOCK) {
                index += 2
            }
        } else {
            val style = when (element) {
                BOLD -> SpanStyle(fontWeight = FontWeight.Bold)
                ITALIC_1, ITALIC_2 -> SpanStyle(fontStyle = FontStyle.Italic)
                CODE, CODE_BLOCK -> SpanStyle(fontFamily = FontFamily.Monospace)
            }

            pushStyle(style)

            if (element == BOLD) {
                index++
            } else if (element == CODE_BLOCK) {
                index += 2
            }

            queue.add(element)
        }

        index++
    }
}

private enum class MarkupElement {
    BOLD,
    ITALIC_1,
    ITALIC_2,
    CODE_BLOCK,
    CODE,
}
