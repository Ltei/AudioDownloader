package com.ltei.audiodownloader.misc

import com.ltei.ljubase.interfaces.ILoadListener
import java.util.concurrent.CompletableFuture

object CompletableFutureUtils {
    var exceptionHandler: ExceptionHandler? = object : ExceptionHandler {
        override fun handle(e: Throwable) {
            e.printStackTrace()
        }
    }

    interface ExceptionHandler {
        fun handle(e: Throwable)
    }
}

fun <T> CompletableFuture<T>.thenAcceptHandling(): CompletableFuture<Void> {
    return this.exceptionally {
        CompletableFutureUtils.exceptionHandler?.handle(it)
        throw it
    }.thenAccept {}
}

inline fun <T> CompletableFuture<T>.thenAcceptHandling(
    crossinline block: (T) -> Unit
): CompletableFuture<Void> {
    return this.exceptionally {
        CompletableFutureUtils.exceptionHandler?.handle(it)
        throw it
    }.thenAccept {
        try {
            block(it)
        } catch (t: Throwable) {
            CompletableFutureUtils.exceptionHandler?.handle(t)
        }
    }
}

inline fun <T> CompletableFuture<T>.thenAcceptHandling(
    crossinline block: (T) -> Unit,
    crossinline fallback: (Throwable) -> Unit
): CompletableFuture<Void> {
    return this.exceptionally {
        CompletableFutureUtils.exceptionHandler?.handle(it)
        fallback(it)
        throw it
    }.thenAccept {
        try {
            block(it)
        } catch (t: Throwable) {
            CompletableFutureUtils.exceptionHandler?.handle(t)
            fallback(t)
        }
    }
}

inline fun <T, U> CompletableFuture<T>.thenApplyHandling(
    crossinline block: (T) -> U
): CompletableFuture<U> {
    return this.exceptionally {
        CompletableFutureUtils.exceptionHandler?.handle(it)
        throw it
    }.thenApply {
        try {
            block(it)
        } catch (t: Throwable) {
            CompletableFutureUtils.exceptionHandler?.handle(t)
            throw t
        }
    }
}

inline fun <T, U> CompletableFuture<T>.thenApplyHandling(
    crossinline block: (T) -> U,
    crossinline fallback: (Throwable) -> Unit
): CompletableFuture<U> {
    return this.exceptionally {
        CompletableFutureUtils.exceptionHandler?.handle(it)
        fallback(it)
        throw it
    }.thenApply {
        try {
            block(it)
        } catch (t: Throwable) {
            CompletableFutureUtils.exceptionHandler?.handle(t)
            fallback(t)
            throw t
        }
    }
}

fun <T, U> CompletableFuture<T>.thenCombineToPair(other: CompletableFuture<U>): CompletableFuture<Pair<T, U>> {
    return this.thenCombine(other) { a, b -> Pair(a, b) }
}


fun <T> List<CompletableFuture<T>>.foldFutures(): CompletableFuture<List<T>> {
    return CompletableFuture.supplyAsync { this.map { it.join() } }
}


fun <T> CompletableFuture<T>.thenAcceptHandling(loadListener: ILoadListener): CompletableFuture<Void> {
    loadListener.onStartLoad()
    return this.exceptionally {
        CompletableFutureUtils.exceptionHandler?.handle(it)
        loadListener.onStopLoad()
        throw it
    }.thenAccept {
        loadListener.onStopLoad()
    }
}

inline fun <T> CompletableFuture<T>.thenAcceptHandling(
    loadListener: ILoadListener,
    crossinline block: (T) -> Unit
): CompletableFuture<Void> {
    loadListener.onStartLoad()
    return this.exceptionally {
        CompletableFutureUtils.exceptionHandler?.handle(it)
        loadListener.onStopLoad()
        throw it
    }.thenAccept {
        try {
            block(it)
            loadListener.onStopLoad()
        } catch (t: Throwable) {
            CompletableFutureUtils.exceptionHandler?.handle(t)
            loadListener.onStopLoad()
        }
    }
}

inline fun <T> CompletableFuture<T>.thenAcceptHandling(
    loadListener: ILoadListener,
    crossinline block: (T) -> Unit,
    crossinline fallback: (Throwable) -> Unit
): CompletableFuture<Void> {
    loadListener.onStartLoad()
    return this.exceptionally {
        CompletableFutureUtils.exceptionHandler?.handle(it)
        loadListener.onStopLoad()
        fallback(it)
        throw it
    }.thenAccept {
        try {
            block(it)
            loadListener.onStopLoad()
        } catch (t: Throwable) {
            CompletableFutureUtils.exceptionHandler?.handle(t)
            loadListener.onStopLoad()
            fallback(t)
        }
    }
}