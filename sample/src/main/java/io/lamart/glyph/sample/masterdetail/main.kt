package io.lamart.glyph.sample.masterdetail

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import android.widget.FrameLayout
import com.badoo.reaktive.subject.behavior.behaviorSubject
import io.lamart.glyph.Dispose
import io.lamart.glyph.Scope


data class Person(val name: String, val age: Int)

data class State(
    val persons: List<Person> = listOf(
        Person("Danny", 28),
        Person("Diede", 26)
    )
)

interface Actions

class MasterDetailActivity : AppCompatActivity() {

    private val subject = behaviorSubject(State())
    private val actions = object : Actions {}
    private lateinit var dispose: Dispose

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = FrameLayout(this) as ViewGroup
        val scope: Scope<Actions, ViewGroup, State, State> = Scope(actions, view, subject)

        dispose = scope.toMainScope() + mainGlyph()
        setContentView(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        dispose()
    }

}