package io.lamart.glyph.sample.basic

import android.app.Application
import com.badoo.reaktive.subject.behavior.behaviorSubject
import io.lamart.glyph.docs.Actions
import io.lamart.glyph.docs.State

class MainApplication : Application() {

    val subject = behaviorSubject(State())
    val actions = Actions(subject)

}