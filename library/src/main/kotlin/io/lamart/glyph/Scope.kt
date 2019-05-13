package io.lamart.glyph

import com.badoo.reaktive.disposable.CompositeDisposable
import com.badoo.reaktive.observable.Observable
import com.badoo.reaktive.observable.subscribe


class Scope<D, P, I, O>(
    val dependencies: D,
    val parent: P,
    private val input: Observable<I>,
    private val output: Observable<O>
) {

    operator fun P.unaryPlus() = withParent(this)
    operator fun plus(parent: P) = withParent(parent)
    fun withParent(parent: P) = Scope(dependencies, parent, input, output)

    operator fun <R> ObservableCompose<I, R>.unaryPlus() = withOutput(this)
    operator fun <R> plus(compose: ObservableCompose<I, R>) = withOutput(compose)
    fun <R> withOutput(compose: ObservableCompose<I, R>) = Scope(dependencies, parent, input, input.let(compose))

    operator fun Glyph<D, P, I, O>.unaryPlus(): Dispose = bind(this)
    operator fun plus(glyph: Glyph<D, P, I, O>): Dispose = bind(glyph)
    fun bind(glyph: Glyph<D, P, I, O>): Dispose =
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

    fun <W, X, Y, Z> map(transform: ScopeTransformer<D, P, I, O>.() -> ScopeTransformer<W, X, Y, Z>): Scope<W, X, Y, Z> =
        ScopeTransformer(dependencies, parent, input, output)
            .let(transform)
            .run { Scope(dependencies, parent, input, output) }


    companion object {

        operator fun <D, P, I> invoke(
            dependencies: D,
            parent: P,
            input: Observable<I>
        ): Scope<D, P, I, I> = Scope(dependencies, parent, input, input)

    }

}
