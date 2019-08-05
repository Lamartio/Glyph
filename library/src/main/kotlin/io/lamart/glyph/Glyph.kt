package io.lamart.glyph

typealias Bind<T> = (next: (model: T) -> Unit) -> Unit
typealias Glyph<A, P, I, O> = Scope<A, P, I, O>.(bind: Bind<O>) -> Dispose