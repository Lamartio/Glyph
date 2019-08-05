package io.lamart.glyph

import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables

typealias Dispose = () -> Unit

val dispose: Dispose = {}
fun dispose(value: Dispose): Dispose = sequenceOf(value).let(::dispose)
fun dispose(vararg values: Dispose): Dispose = values.asSequence().let(::dispose)
fun dispose(values: Iterable<Dispose>): Dispose = values.asSequence().let(::dispose)
fun dispose(values: Sequence<Dispose>): Dispose = values.reduce { l, r -> { l(); r() } }

fun Disposable.toDispose(): Dispose = ::dispose

internal fun Dispose.toDisposable() : Disposable = Disposables.fromAction(dispose)

fun Collection<Dispose>.dispose() = forEach { dispose -> dispose() }