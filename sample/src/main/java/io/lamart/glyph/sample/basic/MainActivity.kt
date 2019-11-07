package io.lamart.glyph.sample.basic

import android.os.Bundle
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import io.lamart.glyph.Dispose
import io.lamart.glyph.GlyphScope
import io.lamart.glyph.invoke
import io.lamart.glyph.sample.basic.glyphs.rootGlyph
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow

class MainActivity : AppCompatActivity() {

    lateinit var disposeRoot: Dispose

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view: ViewGroup = FrameLayout(this)
        val channel = ConflatedBroadcastChannel(State())
        val actions = Actions(channel)
        val scope = GlyphScope(MainScope(), view, actions, channel.asFlow())

        disposeRoot = scope + rootGlyph()
        setContentView(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposeRoot()
    }

}

