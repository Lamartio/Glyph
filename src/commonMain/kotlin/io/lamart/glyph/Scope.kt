package io.lamart.glyph

class Scope<D, P, S, T>(
    val dependencies: D,
    val parent: P,
    private val sender: Sender<S>,
    private val binder: Sender<T>
) {

    operator fun P.unaryPlus() = plus(this)
    operator fun plus(parent: P) = Scope(dependencies, parent, sender, binder)

    operator fun <R> Composer<S, R>.unaryPlus() = plus(this)
    operator fun <R> plus(composer: Composer<S, R>): Scope<D, P, S, R> = sender
        .compose(composer)
        .let { binder -> Scope(dependencies, parent, sender, binder) }

    operator fun Glyph<D, P, S, T>.unaryPlus(): Dispose = plus(this)
    operator fun plus(glyph: Glyph<D, P, S, T>): Dispose =
        DisposableCollection()
            .also { disposables ->
                disposables += glyph.invoke(this) { receiver ->
                    disposables += binder(receiver)
                }
            }
            .toDispose()

}