package io.lamart.glyph

typealias Dispose = () -> Unit

val dispose: Dispose = {}
fun disposeOf(value: Dispose) = value
fun disposeOf(vararg value: Dispose): Dispose = value.asSequence().let(::disposeOf)
fun disposeOf(value: Iterable<Dispose>): Dispose = value.asSequence().let(::disposeOf)
fun disposeOf(value: Sequence<Dispose>): Dispose = value.fold(dispose) { l, r -> { l(); r() } }

fun Iterable<Dispose>.toDispose() = fold(dispose, { l, r -> { l(); r() } })

fun MutableIterable<Dispose>.asDispose() =
    disposeOf {
        fold(dispose, { l, r -> { l(); r() } }).invoke()
    }
