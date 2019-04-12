package io.lamart.glyph

fun <T, R> composer(value: Composer<T, R>): Composer<T, R> = value
fun <T, R> mapDistinct(block: (T) -> R): Composer<T, R> = { it.map(block).distinct() }