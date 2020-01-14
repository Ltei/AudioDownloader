package com.ltei.audiodownloader.misc.util

object ListUtils {

    fun <T> format(
        list: List<T>,
        separator: String = ",",
        transform: ((T) -> Any?)? = null
    ): String = list.withIndex().fold(StringBuilder()) { acc, (idx, it) ->
        if (transform == null) acc.append(it) else acc.append(transform(it))
        if (idx != list.lastIndex) acc.append(separator)
        acc
    }.toString()

    fun <T> concat(vararg lists: List<T>?): List<T> {
        val result = mutableListOf<T>()
        for (list in lists)
            if (list != null)
                result.addAll(list)
        return result
    }

}

fun List<String>?.hasNonBlankString(): Boolean = this?.fold(false) { acc, it -> acc || it.isNotBlank() } ?: false