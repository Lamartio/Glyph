package io.lamart.glyph

import com.badoo.reaktive.disposable.Disposable
import com.badoo.reaktive.disposable.DisposableWrapper

typealias Dispose = () -> Unit

val dispose: Dispose = {}
fun dispose(value: Dispose): Dispose = sequenceOf(value).let(::dispose)
fun dispose(vararg values: Dispose): Dispose = values.asSequence().let(::dispose)
fun dispose(values: Iterable<Dispose>): Dispose = values.asSequence().let(::dispose)
fun dispose(values: Sequence<Dispose>): Dispose = values.reduce { l, r -> { l(); r() } }

fun Disposable.toDispose(): Dispose = ::dispose
fun Dispose.toDisposable(): Disposable = disposableOf(this)

internal fun disposableOf(dispose: Dispose): Disposable =
    DisposableWrapper().also { disposable ->
        disposable.set(object : Disposable {

            override val isDisposed: Boolean
                get() = disposable.isDisposed

            override fun dispose() {
                disposable.dispose()
                dispose.invoke()
            }

        })
    }
