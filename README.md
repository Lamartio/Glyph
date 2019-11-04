[ ![Download](https://api.bintray.com/packages/lamartio/maven/Glyph-Android/images/download.svg) ](https://bintray.com/lamartio/maven/Glyph-Android/_latestVersion)
# Glyph
In web development it is common to create a single object that describes the state of the UI. Every time that state changes, the UI gets updated accordingly. Glyph is using the same principle and wraps it is a simple function which is called... A Glyph!
``` Gradle
implementation "io.lamart.glyph:core-android:+"
```

## How do I use Glyph?
There are only a couple of elements necessary for building an interactive tree of views.
- **A parent view** for adding child views
- **A state** that is used for rendering the views
- **Dependencies** that mostly contain actions for triggering state changes

These elements are bundled in an object called `GlyphScope`. When you check the `GlyphScope` signature you will see it receives 4 type parameters. Lets go over them one by one:

- **A**ctions: As desribed above, these are mostly the actions you need trigger when the user clicks or swipes
- **P**arent: For Android this is usually a ViewGroup since it can add and remove child views.
- **I**nput: The state that is used in your whole presentation layer.
- **O**utput: The state that is used to render a specific part of the view tree. Traditionally this is the state of your `Fragment` or `UIViewController`. This parameter becomes important when your application grows, but initially you don't need to use it.

When you start a new application you will define your presentation state and your actions. If you have an existent app or a boilerplate project, you can use your `Dagger`'s AppComponent as your actions. For simplicity the below examples use a class called `Actions`. 

```kotlin
data class State(val count: Int = 0)

class Actions(private val channel: ConflatedBroadcastChannel<State>) {

    fun increment() =
        update { state -> state.copy(count = state.count + 1) }

    fun decrement() =
        update { state -> state.copy(count = state.count - 1) }

    private fun update(block: (State) -> State) {
        channel.value.let { value ->
            value
                .let(block)
                .takeIf { it != value }
                ?.let { channel.offer(it) }
        }
    }

}
```
With the definition of `State` and `Dependencies` we can fulfill the type parameters of a `GlyphScope` object. Lets fill them in a `typealias` to make the usage simpler. Notice that the `Output` parameter still stays variable. That will come to use later, but for now you can read it as `State`.
```kotlin
typealias SampleGlyphScope<O> = GlyphScope<ViewGroup, Actions, State, O>
```
The `GlyphScope` is used to provision `Glyph` functions and those 'glyphs' are the functional replacement for our traditional `Fragment` or `UIViewController`. So next step is to check the `Glyph` signature:
```kotlin
typealias Glyph<P, A, I, O> = Scope<P, A, I, O>.(bind: Bind<O>) -> Dispose
```
It is a quiet complex function definition, so lets break it down:
- The previously described Scope will function as `this` within the function.
- The `Bind<O>` will be covered later and can be ignored for now.
- The return value is a `Dispose` which is just a function `() -> Unit` that will be called to get rid of this `Glyph`. It fulfills the same function as `Fragment.onDestroyView` or `UIViewController.viewDidDissapear` in the sense it reverts all the work you did in `Fragment.onCreateView` or `UiViewController.viewWillAppear`.

Again the syntax of a `Glyph` is quiet complex, but we can fill in the type parameters to make it simpler:
```kotlin
typealias SampleGlyph<O> = Glyph<ViewGroup, Actions, State, O>
```
Now all the preparation is done and we are ready to create user interface. Each `Glyph` represent a part of the view tree and has 4 responsibilities:
1. Creating the Views
1. Set up interactivity (clicks, swipes etc.)
1. Render state
4. Return instructions of how to undo the previous steps.

Lets create a `Glyph` that renders the `State.count` and has two buttons that trigger `Actions.increment` and `Actions.decrement` respectively.
```kotlin
fun counterGlyph(): SampleGlyph<Int> =
    { bind: Bind<Int> ->
        val layout = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.counter, parent, false)
            .also(parent::addView)
        val countView: TextView = layout.findViewById(R.id.count)
        val plusView: TextView = layout.findViewById(R.id.plus)
        val minusView: TextView = layout.findViewById(R.id.minus)

        plusView.setOnClickListener { actions.increment() }
        minusView.setOnClickListener { actions.decrement() }

        bind { count: Int ->
            countView.text = count.toString()
        }

        disposeOf { parent.removeView(layout) }
    }
```
On the first line we define a `SampleGlyph` that will render an `Int` that is part of `State.count`.

On the second line we see the definition of `Bind` and almost at the end of the function we see it is being used to set the text of a `TextView`. `Bind` is a function that takes a function that takes the desired state that needs to be rendered. That sounds complicated but it's usage is simple: Just put in a lambda and you'll receive the state every time you need to render.

On the last line we give instruction of how to get rid of the part of the UI. The usage will be clear later, but for now you can imagine it will be called in the `Fragment.onDestroyView` or `UIViewController.viewDidDissapear`.

There is one problem left: The `counterGlyph` receives an `Int` but our application is based on `State`. To fix this, we need the map `State` to `State.count`. The mapping happens within a glyph so lets create our initial Glyph:
```kotlin
fun counterGlyph(): SampleGlyph<Int> =
    { bind: Bind<Int> ->
        val layout = LayoutInflater
            .from(parent.context)
            .inflate(io.lamart.glyph.sample.R.layout.counter, parent, false)
            .also(parent::addView)
        val countView: TextView = layout.findViewById(io.lamart.glyph.sample.R.id.count)
        val plusView: TextView = layout.findViewById(io.lamart.glyph.sample.R.id.plus)
        val minusView: TextView = layout.findViewById(io.lamart.glyph.sample.R.id.minus)

        plusView.setOnClickListener { actions.increment() }
        minusView.setOnClickListener { actions.decrement() }

        bind { count: Int ->
            countView.text = count.toString()
        }

        disposeOf { parent.removeView(layout) }
    }

```
The first lines are like the `counterGlyph()`: It adds a views to the parent and caches the crucial views. The end is also as we expect: It creates a function that undoes what we did before.

But something weird is happening at the line of `disposeCounter`. We use math and in the end we call the `counterGlyph()`. Looks like sorcery, but if we break it down it will make sense. 

The goal is to add the `counterGlyph()` to the `rootGlyph()`. Add and plus are almost the same thing and Kotlin allows to replace `.plus()` with a `+`. That explains the last `+`, but that leaves us with two more `+`:  
1. **+content**: Will replace the current parent with `content`, so now the `counterGlyph` will be added to `content`
1. **+state { it.count }**: Will map the `State` to `State.count`. As a result the `GlyphScope` will look like `SampleScope<Int>` which is matching the signature of the `rootGlyph()`.

The trick is that the first two `+` will create new `GlyphScope` objects. So if we write it verbose it would be:
```kotlin
val contentScope: SampleGlyphScope<State> = this + content
val countScope: SampleGlyphScope<Int> = contentScope + state { it.count }
val disposeCounter: Dispose = countScope + counterGlyph()
```

Dependent on which platform we're developing we need to initialize a `GlyphScope` that can trigger the initial `Glyph`. As of now only Android is supported so lets create an Activity:
```kotlin
class MainActivity : AppCompatActivity() {

    lateinit var disposeRoot: Dispose

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view: ViewGroup = FrameLayout(this)
        val channel = ConflatedBroadcastChannel(State())
        val actions = Actions(channel)
        val scope = GlyphScope(MainScope(), view, actions, channel.asFlow())

        disposeRoot = scope + rootGlyph()
        setContentView(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposeRoot()
    }

}
```
In the `onDestroy` we call the function that holds the logic for removing the `rootGlyph`, which hold the logic for removing the `counterGlyph`.

All of the above code is available in the sample project included in this repo.

# So why is it called Glyph?
The goal of Glyph is to realize simple view tree management for the major platforms: Android, iOS and the web browsers. In those paradigms there are already a lot of words describing compositions: Module, Component, Branch, Element, Node etc.. It is important that it should not conflict on any platform and it is favourable to have a short and simple name. Therefore I chose Glyph.

# License
Copyright 2019 Danny Lamarti

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KINA, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
