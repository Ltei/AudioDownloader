package com.ltei.audiodownloader.misc

import java.io.InputStream
import java.net.URL

class Resource(val name: String) : StreamProvider {
    override fun tryOpenStream(): InputStream? = javaClass.getResourceAsStream(name)
    fun openURL(): URL? = javaClass.getResource(name)
}