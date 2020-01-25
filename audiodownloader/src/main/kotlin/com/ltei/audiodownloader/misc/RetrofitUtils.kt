package com.ltei.audiodownloader.misc

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.CompletableFuture

fun <T> Call<T>.toFuture(): CompletableFuture<T> {
    val future = CompletableFuture<T>()
    enqueue(FutureCompletionCallback(future))
    return future
}

class FutureCompletionCallback<T>(val future: CompletableFuture<T>): Callback<T> {
    override fun onFailure(call: Call<T>, t: Throwable) {
        future.completeExceptionally(t)
    }

    override fun onResponse(call: Call<T>, response: Response<T>) {
        if (response.isSuccessful) {
            val body = response.body()
            if (body == null) {
                future.completeExceptionally(NullBodyException())
            } else future.complete(body)
        } else {
            future.completeExceptionally(FailureResponseException(response.code()))
        }
    }
}

class FailureResponseException(val code: Int): Exception()
class NullBodyException: Exception()