package io.lamart.glyph

typealias Bind<T> = (receiver: Receiver<T>) -> Unit
typealias Glyph<D, P, S, T> = (scope: Scope<D, P, S, T>, bind: Bind<T>) -> Dispose

