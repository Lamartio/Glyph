package io.lamart.glyph

interface Binder<T, V> {

    val bind: Bind<T>
    val owner: V

}