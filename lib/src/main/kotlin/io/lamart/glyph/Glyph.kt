package io.lamart.glyph

typealias Bind<T> = (receiver: Receiver<T>) -> Unit
typealias Glyph<D, P, S, T> = (scope: Scope<D, P, S, T>, bind: Bind<T>) -> Dispose

fun <D, P, S, T> glyph(block: Scope<D, P, S, T>.(bind: Bind<T>) -> Dispose): Glyph<D, P, S, T> =
    { scope, bind -> block(scope, bind) }

fun <D, P, S, T> glyph(block: Scope<D, P, S, T>.() -> Dispose): Glyph<D, P, S, T> =
    { scope, _ -> block(scope) }
