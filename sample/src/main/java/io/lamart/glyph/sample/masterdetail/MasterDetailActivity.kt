package io.lamart.glyph.sample.masterdetail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import android.widget.FrameLayout
import io.lamart.glyph.Dispose
import io.lamart.glyph.GlyphScope
import io.lamart.glyph.invoke
import io.lamart.glyph.sample.masterdetail.glyphs.mainGlyph
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow

class MasterDetailActivity : AppCompatActivity() {

    private lateinit var dispose: Dispose

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val channel = ConflatedBroadcastChannel(State())
        val view = FrameLayout(this) as ViewGroup
        val scope: GlyphScope<ViewGroup, Actions, State, State> = GlyphScope(
            MainScope(),
            view,
            Actions { reduce -> channel.value.let(reduce) },
            channel.asFlow()
        )

        dispose = scope + mainGlyph()
        setContentView(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        dispose()
    }

}