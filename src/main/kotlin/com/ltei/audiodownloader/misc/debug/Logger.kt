package com.ltei.audiodownloader.misc.debug

class Logger(val clazz: Class<*>) {
    fun debug(message: Any?) {
        println("[${clazz.simpleName}] $message")
    }
}