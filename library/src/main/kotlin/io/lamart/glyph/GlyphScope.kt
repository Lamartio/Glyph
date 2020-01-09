package io.lamart.glyph

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlin.coroutines.CoroutineContext

class GlyphScope<P, R, I, O> internal constructor(
    val parent: P,
    val resources: R,
    private val input: Flow<I>,
    private val output: Flow<O>,
    private val context: CoroutineContext
) {

    operator fun P.unaryPlus(): GlyphScope<P, R, I, O> = plus(this)
    operator fun plus(parent: P): GlyphScope<P, R, I, O> =
        GlyphScope(parent, resources, input, output, context)

    operator fun <T> Compose<I, T>.unaryPlus(): GlyphScope<P, R, I, T> = plus(this)
    operator fun <T> plus(compose: Compose<I, T>): GlyphScope<P, R, I, T> =
        GlyphScope(parent, resources, input, compose(input), context)

    operator fun Glyph<P, R, I, O>.unaryPlus(): Dispose = plus(this)
    operator fun plus(glyph: Glyph<P, R, I, O>): Dispose {
        val scope = CoroutineScope(context + Job())
        val disposeGlyph = glyph(this) { onBind ->
            scope.launch {
                output.collect {
                    onBind(it)
                }
            }
        }

        return disposeOf { scope.cancel() } + disposeGlyph
    }

    fun <P_, R_, I_, O_> map(transform: (parent: P, resources: R, input: Flow<I>, output: Flow<O>, context: CoroutineContext) -> GlyphScope<P_, R_, I_, O_>): GlyphScope<P_, R_, I_, O_> =
        transform(parent, resources, input, output, context)

    companion object {

        operator fun <P, R, I> invoke(
            parent: P,
            resources: R,
            input: Flow<I>,
            context: CoroutineContext = Dispatchers.Main
        ) = GlyphScope(parent, resources, input, input, context)

    }

}
