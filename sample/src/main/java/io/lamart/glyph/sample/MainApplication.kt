package io.lamart.glyph.sample

import android.app.Application
import com.badoo.reaktive.subject.behavior.behaviorSubject

class MainApplication : Application() {

    val state = State()
    val subject = behaviorSubject(state)
    val actions = Actions(subject)

}