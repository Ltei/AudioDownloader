package com.ltei.audiodownloader.misc.debug

import java.util.*

class Logger(val clazz: Class<*>) {
    fun debug(message: Any?) {
        println("${System.currentTimeMillis()} [${clazz.simpleName}] $message")
    }
}