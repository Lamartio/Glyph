package io.lamart.glyph

class Scope<D, P, I, O>(
    val dependencies: D,
    val parent: P,
    private val input: Sender<I>,
    private val output: Sender<O>
) {

    operator fun P.unaryPlus() = plus(this)
    operator fun plus(parent: P) = Scope(dependencies, parent, input, output)

    operator fun <R> Composer<I, R>.unaryPlus() = plus(this)
    operator fun <R> plus(composer: Composer<I, R>): Scope<D, P, I, R> = input
        .compose(composer)
        .let { binder -> Scope(dependencies, parent, input, binder) }

    operator fun Glyph<D, P, I, O>.unaryPlus(): Dispose = plus(this)
    operator fun plus(glyph: Glyph<D, P, I, O>): Dispose =
        DisposableCollection()
            .also { disposables ->
                disposables += glyph.invoke(this) { receiver ->
                    disposables += output(receiver)
                }
            }
            .toDispose()

    fun <W, X, Y, Z> map(
        block: (dependencies: D, parent: P, input: Sender<I>, output: Sender<O>) -> Scope<W, X, Y, Z>
    ) = block(dependencies, parent, input, output)

}