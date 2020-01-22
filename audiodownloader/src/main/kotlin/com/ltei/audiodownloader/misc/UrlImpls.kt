package com.ltei.audiodownloader.misc

import java.net.URL

fun URL.getParsedQuery(): Map<String, String> {
    val query = query
    if (query.isNullOrEmpty()) return mapOf()
    val querySplit = query.split('&')
    val result = mutableMapOf<String, String>()
    for (arg in querySplit) {
        val argSplit = arg.split('=')
        result[argSplit[0]] = argSplit[1]
    }
    return result
}