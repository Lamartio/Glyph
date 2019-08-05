package io.lamart.glyph

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

typealias Compose<I, O> = (input: Observable<I>) -> Observable<O>

class Scope<A, P, I, O>(
    val actions: A,
    val parent: P,
    private val input: Observable<I>,
    private val compose: Compose<I, O>
) {

    operator fun P.unaryPlus() = plus(this)
    operator fun plus(parent: P) = Scope(actions, parent, input, compose)

    operator fun <R> Compose<I, R>.unaryPlus() = plus(this)
    operator fun <R> plus(compose: Compose<I, R>) = Scope(actions, parent, input, compose)

    operator fun Glyph<A, P, I, O>.unaryPlus() = plus(this)
    operator fun plus(glyph: Glyph<A, P, I, O>) =
        CompositeDisposable()
            .also { disposables ->
                glyph
                    .invoke(this) { next ->
                        input
                            .let(compose)
                            .subscribe(next)
                            .let(disposables::add)
                    }
                    .toDisposable()
                    .let(disposables::add)
            }
            .toDispose()

    fun <A_, P_, I_, O_> map(transform: (actions: A, parent: P, input: Observable<I>, compose: Compose<I, O>) -> Scope<A_, P_, I_, O_>) =
        transform(actions, parent, input, compose)

    companion object {

        operator fun <A, P, I> invoke(
            actions: A,
            parent: P,
            input: Observable<I>
        ): Scope<A, P, I, I> = Scope(actions, parent, input, { it })

    }

}
