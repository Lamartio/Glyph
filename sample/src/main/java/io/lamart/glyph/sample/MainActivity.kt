package io.lamart.glyph.sample

import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.CoordinatorLayout.LayoutParams.*
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity;
import android.view.*
import android.view.Gravity.*
import android.widget.FrameLayout
import android.widget.TextView
import io.lamart.glyph.*

class MainActivity : AppCompatActivity() {

    private lateinit var dispose: Dispose

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = FrameLayout(this).apply {
            id = View.generateViewId()
            setContentView(this)
        }
        val app = application.let { it as MainApplication }
        val scope = Scope(app.actions, view as ViewGroup, app.subject, app.subject)

        dispose = scope + mainGlyph()
    }

    override fun onDestroy() {
        super.onDestroy()
        dispose()
    }

}

fun mainGlyph(): SampleGlyph<State> = sampleGlyph {
    val inflater = LayoutInflater.from(context)
    val view = inflater
        .inflate(R.layout.main, parent, false)
        .also(parent::addView)
    val content = view.findViewById<ViewGroup>(R.id.content)
    val disposeButton = +content + incrementGlyph()
    val disposeCounter = +content + state { it.count } + counterGlyph()

    dispose(
        disposeCounter,
        disposeButton,
        { parent.removeView(view) }
    )
}

fun counterGlyph() = sampleGlyph<Int> { bind ->
    val view = TextView(context).also(parent::addView)

    bind { count ->
        view.text = count.toString()
    }

    dispose { parent.removeView(view) }
}


fun incrementGlyph(): SampleGlyph<State> = sampleGlyph {
    val view = FloatingActionButton(context)
        .apply {
            layoutParams = CoordinatorLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                val density = context.resources.displayMetrics.density
                val margin = 16f.times(density).toInt()

                gravity = BOTTOM or END
                marginEnd = margin
                bottomMargin = margin
            }
        }
        .also(parent::addView)

    view.setOnClickListener {
        actions.increment()
    }

    dispose { parent.removeView(view) }
}
