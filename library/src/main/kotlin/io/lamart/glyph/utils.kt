package io.lamart.glyph

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlin.math.roundToInt

typealias Compose<I, O> = (input: Flow<I>) -> Flow<O>
typealias Bind<O> = (OnBind<O>) -> Unit
typealias OnBind<O> = (O) -> Unit

fun <I, O> GlyphScope<*, *, I, *>.outputOf(transform: suspend (I) -> O): Compose<I, O> =
    { input -> input.map(transform).distinctUntilChanged() }

fun <P, R, I, O> GlyphScope<P, R, I, O>.glyphOf(glyph: Glyph<P, R, I, I>): Glyph<P, R, I, O> =
    glyphOf(output = { it }, glyph = glyph)

@JvmName("glyphOfTransform")
fun <P, R, I, O, T> GlyphScope<P, R, I, O>.glyphOf(
    output: suspend (I) -> T,
    glyph: Glyph<P, R, I, T>
): Glyph<P, R, I, O> =
    glyphOf(compose = outputOf(output), glyph = glyph)

@JvmName("glyphOfCompose")
fun <P, R, I, O, T> GlyphScope<P, R, I, O>.glyphOf(
    compose: Compose<I, T>,
    glyph: Glyph<P, R, I, T>
): Glyph<P, R, I, O> = { +compose + glyph }
