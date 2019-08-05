package io.lamart.glyph.android.viewpager

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import io.lamart.glyph.Dispose

abstract class GlyphPagerAdapter<T> : PagerAdapter() {

    private var dataSet: List<T> = emptyList()

    fun notifyDataSetChanged(dataSet: List<T>) {
        this.dataSet = dataSet
        notifyDataSetChanged()
    }

    override fun getPageTitle(position: Int): CharSequence? = getPageTitle(dataSet[position], position)

    open fun getPageTitle(data: T, position: Int): CharSequence? = data.toString()

    @Deprecated("Should use its overload")
    override fun instantiateItem(container: ViewGroup, position: Int): Any =
        dataSet[position].let { data ->
            instantiateItem(container, data, position).toObject(data, position)
        }

    abstract fun instantiateItem(container: ViewGroup, data: T, position: Int): Item

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        if (`object` is Object<*>) {
            `object`.dispose()
            container.removeView(`object`.parent)
        } else {
            super.destroyItem(container, position, `object`)
        }
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean =
        `object` is Object<*> && view == `object`.parent

    override fun getItemPosition(item: Any): Int =
        if (item is Object<*>) {
            when (dataSet.indexOf(item.data)) {
                -1 -> POSITION_NONE
                item.position -> POSITION_UNCHANGED
                else -> item.position
            }
        } else {
            super.getItemPosition(item)
        }

    override fun getCount(): Int = dataSet.size

    private fun <T> Item.toObject(data: T, position: Int): Object<T> =
        Object(frame, data, position, dispose)

    data class Item(val frame: ViewGroup, val dispose: Dispose)

    private class Object<T>(
        val parent: ViewGroup,
        val data: T,
        val position: Int,
        val dispose: Dispose
    )

    companion object {

        operator fun <T> invoke(instantiateItem: (container: ViewGroup, data: T, position: Int) -> Item): GlyphPagerAdapter<T> =
            object : GlyphPagerAdapter<T>() {
                override fun instantiateItem(container: ViewGroup, data: T, position: Int): Item =
                    instantiateItem.invoke(container, data, position)
            }

    }

}