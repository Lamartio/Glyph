package io.lamart.glyph

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

typealias Compose<I, O> = (input: Observable<I>) -> Observable<O>

class Scope<D, P, I, O>(
    val dependencies: D,
    val parent: P,
    private val input: Observable<I>,
    private val compose: Compose<I, O>
) {

    operator fun P.unaryPlus() = plus(this)
    operator fun plus(parent: P) = Scope(dependencies, parent, input, compose)

    operator fun <R> Compose<I, R>.unaryPlus() = plus(this)
    operator fun <R> plus(compose: Compose<I, R>) = Scope(dependencies, parent, input, compose)

    operator fun Glyph<D, P, I, O>.unaryPlus() = plus(this)
    operator fun plus(glyph: Glyph<D, P, I, O>) =
        CompositeDisposable()
            .also { disposables ->
                glyph
                    .invoke(this) { next ->
                        input
                            .let(compose)
                            .subscribe(next)
                            .let(disposables::add)
                    }
                    .let(::disposableOf)
                    .let(disposables::add)
            }
            .toDispose()

    fun <D_, P_, I_, O_> map(transform: (dependencies: D, parent: P, input: Observable<I>, compose: Compose<I, O>) -> Scope<D_, P_, I_, O_>) =
        transform(dependencies, parent, input, compose)

    companion object {

        operator fun <D, P, I> invoke(
            dependencies: D,
            parent: P,
            input: Observable<I>
        ): Scope<D, P, I, I> = Scope(dependencies, parent, input, { it })

    }

}
