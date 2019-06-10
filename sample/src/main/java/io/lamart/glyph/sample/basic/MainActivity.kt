package io.lamart.glyph.sample.basic

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import io.lamart.glyph.Dispose
import io.lamart.glyph.Scope
import io.lamart.glyph.sample.basic.glyphs.mainGlyph

class MainActivity : AppCompatActivity() {

    private lateinit var dispose: Dispose

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = FrameLayout(this)
            .apply { id = View.generateViewId() }
        val app = application.let { it as MainApplication }
        val scope = Scope(app.actions, view as ViewGroup, app.subject)

        dispose = scope + mainGlyph()
        setContentView(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        dispose()
    }

}


