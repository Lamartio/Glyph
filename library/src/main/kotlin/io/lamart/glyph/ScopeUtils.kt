package io.lamart.glyph

import io.reactivex.Observable

fun <D, P, I, O, R> Scope<D, P, I, O>.withDependencies(dependencies: R): Scope<R, P, I, O> =
    map { _, parent, input, compose ->
        Scope(dependencies, parent, input, compose)
    }

fun <D, P, I, O, R> Scope<D, P, I, O>.withParent(parent: R): Scope<D, R, I, O> =
    map { dependencies, _, input, compose ->
        Scope(dependencies, parent, input, compose)
    }

fun <D, P, I, O, R> Scope<D, P, I, O>.withInput(input: Observable<R>): Scope<D, P, R, R> =
    map { dependencies, parent, _, _ ->
        Scope(dependencies, parent, input)
    }


fun <D, P, I, O, R> Scope<D, P, I, O>.mapDependencies(transform: (D) -> R): Scope<R, P, I, O> =
    map { dependencies, parent, input, compose ->
        Scope(transform(dependencies), parent, input, compose)
    }

fun <D, P, I, O, R> Scope<D, P, I, O>.mapParent(transform: (P) -> R): Scope<D, R, I, O> =
    map { dependencies, parent, input, compose ->
        Scope(dependencies, transform(parent), input, compose)
    }

fun <D, P, I, O, R> Scope<D, P, I, O>.mapInput(transform: Compose<I, R>): Scope<D, P, R, R> =
    map { dependencies, parent, input, _ ->
        Scope(dependencies, parent, transform(input))
    }

fun <D, P, I, O, R> Scope<D, P, I, O>.mapOutput(transform: Compose<O, R>): Scope<D, P, I, R> =
    map { dependencies, parent, input, compose ->
        Scope(dependencies, parent, input) { it.let(compose).let(transform) }
    }
