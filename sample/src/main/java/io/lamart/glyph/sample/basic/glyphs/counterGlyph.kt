package io.lamart.glyph.sample.basic.glyphs

import android.view.LayoutInflater
import android.widget.TextView
import io.lamart.glyph.Bind
import io.lamart.glyph.disposeOf
import io.lamart.glyph.sample.basic.SampleGlyph

fun counterGlyph(): SampleGlyph<Int> =
    { bind: Bind<Int> ->
        val layout = LayoutInflater
            .from(parent.context)
            .inflate(io.lamart.glyph.sample.R.layout.counter, parent, false)
            .also(parent::addView)
        val countView: TextView = layout.findViewById(io.lamart.glyph.sample.R.id.count)
        val plusView: TextView = layout.findViewById(io.lamart.glyph.sample.R.id.plus)
        val minusView: TextView = layout.findViewById(io.lamart.glyph.sample.R.id.minus)

        plusView.setOnClickListener { resources.increment() }
        minusView.setOnClickListener { resources.decrement() }

        bind { count: Int ->
            countView.text = count.toString()
        }

        disposeOf { parent.removeView(layout) }
    }
