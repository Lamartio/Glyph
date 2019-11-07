package io.lamart.glyph.boilerplate /* Adjust this to your own namespace */

import io.lamart.glyph.Compose
import io.lamart.glyph.glyphOf

private typealias Parent = Any /* Can be Android's ViewGroup, iOS's UIView or browser's HTMLElement  */
private typealias Actions = Any /* The interface that contains everything UI can trigger */
private typealias Input = Any /* The type of data you want to pass to the presentation layer (usually a data class) */

typealias Glyph<O> = io.lamart.glyph.Glyph<Parent, Actions, Input, O>
typealias GlyphScope<O> = io.lamart.glyph.GlyphScope<Parent, Actions, Input, O>

fun <O> GlyphScope<O>.glyphOf(glyph: Glyph<Input>): Glyph<O> =
    glyphOf(glyph)

@JvmName("glyphOfTransform")
fun <O, R> GlyphScope<O>.glyphOf(transform: (Input) -> R, glyph: Glyph<R>): Glyph<O> =
    glyphOf(transform, glyph)

@JvmName("glyphOfCompose")
fun <O, R> GlyphScope<O>.glyphOf(compose: Compose<Input, R>, glyph: Glyph<R>): Glyph<O> =
    glyphOf(compose, glyph)