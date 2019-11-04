package io.lamart.glyph

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class GlyphScope<P, A, I, O>(
    private val scope: CoroutineScope,
    val parent: P,
    val actions: A,
    private val input: Flow<I>,
    private val output: Flow<O>
) {

    operator fun <R> R.unaryPlus(): GlyphScope<R, A, I, O> = plus(this)
    operator fun <R> plus(parent: R): GlyphScope<R, A, I, O> =
        GlyphScope(scope, parent, actions, input, output)

    operator fun <R> Compose<I, R>.unaryPlus(): GlyphScope<P, A, I, R> = plus(this)
    operator fun <R> plus(compose: Compose<I, R>): GlyphScope<P, A, I, R> =
        GlyphScope(scope, parent, actions, input, compose(input))

    operator fun Glyph<P, A, I, O>.unaryPlus(): Dispose = plus(this)
    operator fun plus(glyph: Glyph<P, A, I, O>): Dispose {
        val jobs = mutableListOf<Dispose>()
        val disposeGlyph = glyph(this) { receive ->
            scope
                .launch { output.collect { receive(it) } }
                .toDispose()
                .let(jobs::add)
        }

        return disposeOf(
            jobs.asDispose(),
            disposeGlyph
        )
    }

    fun <P_, A_, I_, O_> map(transform: (scope: CoroutineScope, parent: P, actions: A, input: Flow<I>, output: Flow<O>) -> GlyphScope<P_, A_, I_, O_>): GlyphScope<P_, A_, I_, O_> =
        transform(scope, parent, actions, input, output)

    companion object

}

private fun Job.toDispose() = disposeOf { cancel() }

operator fun <P, A, I> GlyphScope.Companion.invoke(
    scope: CoroutineScope,
    parent: P,
    actions: A,
    input: Flow<I>
): GlyphScope<P, A, I, I> = GlyphScope(scope, parent, actions, input, input)