package io.lamart.glyph.android.recyclerview

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.lamart.glyph.Compose
import io.lamart.glyph.Dispose
import io.lamart.glyph.GlyphScope


abstract class RecyclerGlyphAdapter<D, I, O>(
    private val disposes: MutableList<Dispose> = mutableListOf()
) : RecyclerView.Adapter<RecyclerGlyphAdapter.ViewHolder<O>>()
    , RecyclerGlyphAdapterType<D, I, O>
    , List<Dispose> by disposes {

    final override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<O> {
        val subject = PublishSubject.create<O>()
        var holder: ViewHolder<O>? = null
        var dispose: Dispose? = null

        onCreateGlyph(viewType) { parent, build ->
            holder = ViewHolder(parent, subject::onNext)
            dispose = onCreateScope(parent, { subject }).let(build)
        }

        disposes += subject::onComplete
        dispose
            ?.let(disposes::add)
            ?: throw IllegalStateException("Dispose may not be null. Did you forget to fullfill onCreateScope?")

        return holder ?: throw IllegalStateException("Holder may not be null. Did you forget to fullfill onCreateScope?")
    }

    class ViewHolder<O> internal constructor(view: View, val bind: (O) -> Unit) : RecyclerView.ViewHolder(view)

    abstract class SimpleInstance<D, I, O>(
        private val scope: GlyphScopeInstance<D, ViewGroup, I, List<O>>,
        override val list: DiffList<O> = DiffList(),
        disposes: MutableList<Dispose> = mutableListOf()
    ) : RecyclerGlyphAdapter<D, I, O>(disposes), DiffListAdapter<O> {

        override fun onCreateScope(parent: ViewGroup, compose: Compose<I, O>): GlyphScopeInstance<D, ViewGroup, I, O> =
            GlyphScopeInstance(scope.actions, parent, Observable.empty(), compose)

        override fun getItemCount(): Int = list.size

        override fun onBindViewHolder(holder: ViewHolder<O>, position: Int) =
            list[position].let(holder.bind)

        override fun notifyDataSetChanged(dataSet: List<O>) = list.set(dataSet).dispatchUpdatesTo(this)
    }

}
