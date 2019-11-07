package io.lamart.glyph.sample.masterdetail.glyphs

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import io.lamart.glyph.Glyph
import io.lamart.glyph.OnBind
import io.lamart.glyph.android.recyclerview.OnCreateViewHolder
import io.lamart.glyph.android.recyclerview.recyclerViewDelegates
import io.lamart.glyph.disposeOf
import io.lamart.glyph.sample.masterdetail.Actions
import io.lamart.glyph.sample.masterdetail.Person
import io.lamart.glyph.sample.masterdetail.State

fun personsGlyph(): Glyph<ViewGroup, Actions, State, List<Person>> = { bind ->
    val view = RecyclerView(parent.context)
    val (disposeRecyclerView, onCreateViewHolder) = recyclerViewDelegates { personsItemGlyph() }
    val personsAdapter = PersonsAdapter(onCreateViewHolder)

    view.layoutManager = LinearLayoutManager(parent.context)
    view.adapter = personsAdapter
    view.setBackgroundResource(android.R.color.darker_gray)
    parent.addView(view, ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT))

    bind(personsAdapter::notifyDataSetChanged)

    disposeOf(
        { parent.removeView(view) },
        disposeRecyclerView
    )
}

private class PersonsAdapter(private val onCreateViewHolder: OnCreateViewHolder<ViewGroup, Person>) :
    RecyclerView.Adapter<PersonsAdapter.ViewHolder>() {

    private var persons = emptyList<Person>()

    override fun onCreateViewHolder(container: ViewGroup, viewType: Int): ViewHolder {
        val itemView = FrameLayout(container.context)
        val onBind = onCreateViewHolder.invoke(itemView, viewType)

        return ViewHolder(itemView, onBind)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) =
        persons[position].let(viewHolder.onBind)

    override fun getItemCount(): Int = persons.size

    fun notifyDataSetChanged(persons: List<Person>) {
        this.persons = persons
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View, val onBind: OnBind<Person>) : RecyclerView.ViewHolder(itemView)

}
