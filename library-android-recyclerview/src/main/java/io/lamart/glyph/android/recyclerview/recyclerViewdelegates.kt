package io.lamart.glyph.android.recyclerview

import io.lamart.glyph.*
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.emptyFlow

typealias OnCreateViewHolder<P, O> = (itemView: P, viewType: Int) -> OnBind<O>

data class RecyclerViewDelegates<P, O>(
    val dispose: Dispose,
    val onCreateViewHolder: OnCreateViewHolder<P, O>
)

fun <P, R, I, O> GlyphScope<P, R, I, *>.recyclerViewDelegates(glyphFactory: (viewType: Int) -> Glyph<P, R, I, O>): RecyclerViewDelegates<P, O> {
    val disposes = mutableListOf<Dispose>()

    return RecyclerViewDelegates(disposes.asDispose()) { itemView, viewType ->
        val channel = ConflatedBroadcastChannel<O>()
        val scope: GlyphScope<P, R, I, O> = map { _, resources, _, _, context ->
            GlyphScope(itemView, resources, emptyFlow<I>(), channel.asFlow(), context)
        }
        val disposeChannel = disposeOf { channel.close() }
        val disposeGlyph = scope + glyphFactory(viewType)

        disposes += disposeOf(disposeChannel, disposeGlyph);

        { item -> channel.offer(item) }
    }
}
