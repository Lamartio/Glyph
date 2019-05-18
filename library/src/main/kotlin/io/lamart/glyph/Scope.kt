package io.lamart.glyph

import com.badoo.reaktive.disposable.CompositeDisposable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.subscribe

typealias Compose<T, R> = (Observable<T>) -> Observable<R>

class Scope<D, P, I, O> private constructor(
    val dependencies: D,
    val parent: P,
    private val input: Observable<I>,
    private val output: Observable<O>
) {

    operator fun P.unaryPlus() = withParent(this)
    operator fun plus(parent: P) = withParent(parent)
    fun withParent(parent: P) = Scope(dependencies, parent, input, output)

    operator fun <R> Compose<I, R>.unaryPlus() = withOutput(this)
    operator fun <R> plus(compose: Compose<I, R>) = withOutput(compose)
    fun <R> withOutput(compose: Compose<I, R>) = Scope(dependencies, parent, input, input.let(compose))

    operator fun Glyph<D, P, I, O>.unaryPlus() = bind(this)
    operator fun plus(glyph: Glyph<D, P, I, O>) = bind(glyph)
    fun bind(glyph: Glyph<D, P, I, O>) =
        CompositeDisposable()
            .also { disposables ->
                glyph
                    .invoke(this) { next ->
                        output
                            .subscribe(onNext = next)
                            .let(disposables::add)
                    }
                    .let(::disposableOf)
                    .let(disposables::add)
            }
            .toDispose()

    fun <W, X, Y, Z> map(transform: (dependencies: D, parent: P, input: Observable<I>) -> Scope<W, X, Y, Z>) =
        transform(dependencies, parent, input)

    companion object {

        operator fun <D, P, I> invoke(
            dependencies: D,
            parent: P,
            input: Observable<I>
        ): Scope<D, P, I, I> = Scope(dependencies, parent, input, input)

    }

}
