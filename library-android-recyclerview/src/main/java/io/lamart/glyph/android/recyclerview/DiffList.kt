package io.lamart.glyph.android.recyclerview

import androidx.recyclerview.widget.DiffUtil

class DiffList<T>(
    private val areItemsTheSame: (oldItem: T, newItem: T) -> Boolean = { oldItem, newItem -> oldItem === newItem },
    private val areContentsTheSame: (oldItem: T, newItem: T) -> Boolean = { oldItem, newItem -> oldItem == newItem },
    private val detectMoves: Boolean = true,
    private val newItems: MutableList<T> = mutableListOf()
) : DiffUtil.Callback(), List<T> by newItems {

    private val oldItems: MutableList<T> = mutableListOf()

    fun set(items: List<T>): DiffUtil.DiffResult {
        oldItems.apply { clear(); addAll(newItems) }
        newItems.apply { clear(); addAll(items) }

        return DiffUtil.calculateDiff(this, detectMoves)
    }

    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        areItemsTheSame(oldItems[oldItemPosition], newItems[newItemPosition])

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        areContentsTheSame(oldItems[oldItemPosition], newItems[newItemPosition])

}
