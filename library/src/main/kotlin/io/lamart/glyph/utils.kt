package io.lamart.glyph

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

typealias Compose<I, O> = (input: Flow<I>) -> Flow<O>
typealias Bind<O> = (OnBind<O>) -> Unit
typealias OnBind<O> = (O) -> Unit

fun <I, O> GlyphScope<*, *, I, *>.output(transform: suspend (I) -> O): Compose<I, O> =
    { input -> input.map(transform).distinctUntilChanged() }
