package com.ltei.audiodownloader.misc.debug

class IntrusiveBencher(private val logger: Logger) {

    constructor(clazz: Class<*>): this(Logger(clazz))

    private val times = mutableMapOf<String, Long>()

    fun reset() {
        times.clear()
    }

    fun tag(tag: String) {
        times[tag] = System.currentTimeMillis()
    }

    fun printDelta(tag1: String, tag2: String) {
        val delta = times[tag2]!! - times[tag1]!!
        logger.debug("$tag1 to $tag2 -> $delta ms")
    }

    fun printAllDeltas() {
        val timesList = times.toList().sortedBy { it.second }
        for (i in 0 until timesList.lastIndex) {
            val (tag1, time1) = timesList[i]
            val (tag2, time2) = timesList[i + 1]
            logger.debug("$tag1 to $tag2 -> ${time2 - time1} ms")
        }
    }

}