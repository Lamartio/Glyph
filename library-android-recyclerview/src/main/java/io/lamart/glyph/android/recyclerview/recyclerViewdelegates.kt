package io.lamart.glyph.android.recyclerview

import io.lamart.glyph.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow

typealias OnCreateViewHolder<P, O> = (itemView: P, viewType: Int) -> OnBind<O>

data class RecyclerViewDelegates<P, O>(
    val dispose: Dispose,
    val onCreateViewHolder: OnCreateViewHolder<P, O>
)

fun <P, A, I, O> GlyphScope<P, A, I, *>.recyclerViewDelegates(glyphFactory: (viewType: Int) -> Glyph<P, A, I, O>): RecyclerViewDelegates<P, O> {
    val disposes = mutableListOf<Dispose>()

    return RecyclerViewDelegates(disposes.asDispose()) { itemView, viewType ->
        val channel = ConflatedBroadcastChannel<O>()
        val scope: GlyphScope<P, A, I, O> = map { scope, _, actions, input, _ ->
            GlyphScope(scope, itemView, actions, input, channel.asFlow())
        }
        val disposeChannel = disposeOf { channel.close() }
        val disposeGlyph = scope + glyphFactory(viewType)

        disposes += disposeOf(disposeChannel, disposeGlyph);

        { item -> channel.offer(item) }
    }
}
