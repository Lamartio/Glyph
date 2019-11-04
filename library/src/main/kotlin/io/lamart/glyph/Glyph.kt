package io.lamart.glyph


/**
 * A Glyph is a function that maintains a part of a hierarchical structure (usually a UI tree, or also known as a composition). The usual flow within a Glyph is:
 *
 * - It creates children for a given parent.
 * - It binds state to properties of the children.
 * - It calls actions (that in time will update state).
 * - It returns instructions of how to dispose everything that is created.
 *
 *  @param <P> Parent; the parent in which a Glyph should operate. Usually this is a UI element in which a Glyph can add, remove and modify the Parent's children.
 *  @param <A> Actions; the interactivity for a tree of Glyphs.
 *  @param <I> Input; the globally accessible state for a tree of Glyphs.
 *  @param <O> Output; the locally accessible state, used for rendering a Glyph.
 *
 */

typealias Glyph<P, A, I, O> = GlyphScope<P, A, I, O>.(bind: Bind<O>) -> Dispose

fun <P, A, I, O> GlyphScope<P, A, I, O>.glyphOf(glyph: Glyph<P, A, I, I>): Glyph<P, A, I, O> =
    glyphOf(transform = { it }, glyph = glyph)

@JvmName("glyphOfTransform")
fun <P, A, I, O, R> GlyphScope<P, A, I, O>.glyphOf(transform: (I) -> R, glyph: Glyph<P, A, I, R>): Glyph<P, A, I, O> =
    glyphOf(compose = output(transform), glyph = glyph)

@JvmName("glyphOfCompose")
fun <P, A, I, O, R> GlyphScope<P, A, I, O>.glyphOf(
    compose: Compose<I, R>,
    glyph: Glyph<P, A, I, R>
): Glyph<P, A, I, O> = { +compose + glyph }
