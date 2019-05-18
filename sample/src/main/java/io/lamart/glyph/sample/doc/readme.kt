package io.lamart.glyph.sample.doc

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toolbar
import com.badoo.reaktive.subject.behavior.BehaviorSubject
import com.badoo.reaktive.subject.behavior.behaviorSubject
import io.lamart.glyph.*


data class State(val count: Int = 0)

val subject = behaviorSubject(State())

class Actions(val subject: BehaviorSubject<State>) {

    fun increment() =
        update { state -> state.copy(count = state.count + 1) }

    fun decrement() =
        update { state -> state.copy(count = state.count - 1) }

    private fun update(block: (State) -> State) {
        subject.value.let { value ->
            value
                .let(block)
                .takeIf { it != value }
                ?.let(subject::onNext)
        }
    }

}

typealias SampleScope<O> = Scope<Actions, ViewGroup, State, O>
typealias SampleGlyph<O> = Glyph<Actions, ViewGroup, State, O>

fun counterGlyph(): SampleGlyph<Int> =
    { bind: Bind<Int> ->
        val layout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.counter, parent, false)
            .also(parent::addView)
        val countView: TextView = layout.findViewById(R.id.count)
        val plusView: TextView = layout.findViewById(R.id.plus)
        val minusView: TextView = layout.findViewById(R.id.plus)

        plusView.setOnClickListener { dependencies.increment() }
        minusView.setOnClickListener { dependencies.decrement() }

        bind { count: Int ->
            countView.text = count.toString()
        }

        dispose { parent.removeView(layout) }
    }

fun rootGlyph(): SampleGlyph<State> =
    { bind ->
        val layout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.counter, parent, false)
            .also(parent::addView)
        val toolBar: Toolbar = layout.findViewById(R.id.toolBar)
        val content: ViewGroup = layout.findViewById(R.id.content)

        val disposeCounter: Dispose = +content + state { it.count } + counterGlyph()

        dispose(
            disposeCounter,
            { parent.removeView(layout) }
        )
    }

class RootActivity: Activity() {

    lateinit var disposeRoot: Dispose

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view: ViewGroup = FrameLayout(this)
        val subject = behaviorSubject(State())
        val actions = Actions(subject)
        val scope = Scope(actions, view, subject)

        disposeRoot = scope + rootGlyph()
        setContentView(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposeRoot()
    }

}