package io.lamart.glyph.sample

import android.app.Application
import com.badoo.reaktive.subject.behavior.behaviorSubject

class MainApplication : Application() {

    val subject = behaviorSubject(State())
    val actions = Actions(subject)

}