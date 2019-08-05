package io.lamart.glyph.android.recyclerview

interface DiffListAdapter<T> {
    val list: DiffList<T>

    fun getItemCount(): Int = list.size
    fun onBindViewHolder(holder: RecyclerGlyphAdapter.ViewHolder<T>, position: Int) = list[position].let(holder.bind)
    fun notifyDataSetChanged(dataSet: List<T>)
}
