package com.ltei.audiodownloader.misc

import com.ltei.ljubase.debug.Logger
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import org.schabi.newpipe.extractor.downloader.Downloader
import org.schabi.newpipe.extractor.downloader.Request
import org.schabi.newpipe.extractor.downloader.Response
import org.schabi.newpipe.extractor.exceptions.ReCaptchaException
import java.io.IOException




object DownloaderImpl: Downloader() {
    private val userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0"
    private val logger = Logger(DownloaderImpl::class.java)
    private val mCookies: String? = null
    private val client = OkHttpClient.Builder().build()

    /**
     * Get the size of the content that the url is pointing by firing a HEAD request.
     *
     * @param url an url pointing to the content
     * @return the size of the content, in bytes
     */
    fun getContentLength(url: String): Long {
        return try {
            val response = head(url)
            response.getHeader("Content-Length")!!.toLong()
        } catch (e: NumberFormatException) {
            throw IOException("Invalid content length", e)
        } catch (e: ReCaptchaException) {
            throw IOException(e)
        }
    }

    override fun execute(request: Request): Response {
        val httpMethod = request.httpMethod()
        val url = request.url()
        val headers = request.headers()
        val dataToSend = request.dataToSend()

        var requestBody: RequestBody? = null
        if (dataToSend != null) {
            requestBody = RequestBody.create(null, dataToSend)
        }

        val requestBuilder = okhttp3.Request.Builder()
            .method(httpMethod, requestBody).url(url)
            .addHeader("User-Agent", userAgent)

        if (!mCookies.isNullOrEmpty())
            requestBuilder.addHeader("Cookie", mCookies)

        for ((headerName, headerValueList) in headers) {
            if (headerValueList.size > 1) {
                requestBuilder.removeHeader(headerName)
                for (headerValue in headerValueList) {
                    requestBuilder.addHeader(headerName, headerValue!!)
                }
            } else if (headerValueList.size == 1) {
                requestBuilder.header(headerName, headerValueList[0])
            }
        }

        val response = client.newCall(requestBuilder.build()).execute()

        if (response.code == 429) {
            response.close()
            throw ReCaptchaException("reCaptcha Challenge requested", url)
        }

        val body = response.body
        var responseBodyToReturn: String? = null

        if (body != null) {
            responseBodyToReturn = body.string()
        }

        return Response(
            response.code,
            response.message,
            response.headers.toMultimap(),
            responseBodyToReturn
        )
    }
}