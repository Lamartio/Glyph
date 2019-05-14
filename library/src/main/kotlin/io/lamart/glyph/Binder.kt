package io.lamart.glyph

typealias Bind<T> = (next: (model: T) -> Unit) -> Unit

interface Binder<T, V> {

    val bind: Bind<T>
    val owner: V

}