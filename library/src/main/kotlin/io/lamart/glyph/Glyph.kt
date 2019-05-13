package io.lamart.glyph

typealias Glyph<D, P, I, O> = Scope<D, P, I, O>.(bind: Bind<O>) -> Dispose


