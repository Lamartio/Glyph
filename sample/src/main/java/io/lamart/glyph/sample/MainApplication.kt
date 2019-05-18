package io.lamart.glyph.sample

import android.app.Application
import com.badoo.reaktive.subject.behavior.behaviorSubject
import io.lamart.glyph.sample.doc.Actions
import io.lamart.glyph.sample.doc.State

class MainApplication : Application() {

    val subject = behaviorSubject(State())
    val actions = Actions(subject)

}