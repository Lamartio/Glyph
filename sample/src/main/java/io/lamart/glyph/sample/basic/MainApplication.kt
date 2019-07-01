package io.lamart.glyph.sample.basic

import android.app.Application
import io.lamart.glyph.docs.Actions
import io.lamart.glyph.docs.State
import io.reactivex.subjects.BehaviorSubject

class MainApplication : Application() {

    val subject = BehaviorSubject.createDefault(State())
    val actions = Actions(subject)

}