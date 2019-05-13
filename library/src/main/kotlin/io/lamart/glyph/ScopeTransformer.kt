package io.lamart.glyph

import com.badoo.reaktive.observable.Observable

class ScopeTransformer<D, P, I, O> internal constructor(
    val dependencies: D,
    val parent: P,
    val input: Observable<I>,
    val output: Observable<O>
) {

    fun <T> dependencies(dependencies: T): ScopeTransformer<T, P, I, O> =
        ScopeTransformer(dependencies, parent, input, output)

    fun <T> parent(parent: T): ScopeTransformer<D, T, I, O> =
        ScopeTransformer(dependencies, parent, input, output)

    fun <T> input(input: Observable<T>): ScopeTransformer<D, P, T, T> =
        ScopeTransformer(dependencies, parent, input, input)

    fun <T> output(compose: Compose<I, T>): ScopeTransformer<D, P, I, T> =
        ScopeTransformer(dependencies, parent, input, input.let(compose))

}