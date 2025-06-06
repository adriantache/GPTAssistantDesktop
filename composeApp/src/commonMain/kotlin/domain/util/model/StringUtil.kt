package domain.util.model

fun String.capitalizeFirst(): String {
    return this
        .mapIndexed { index, char -> if (index == 0) char.uppercase() else char }
        .joinToString("")
}
