import io.lamart.glyph.Receiver
import io.lamart.glyph.Sender
import io.reactivex.*
import io.reactivex.functions.Consumer
import io.reactivex.internal.functions.Functions.*
import io.reactivex.internal.observers.LambdaObserver
import io.reactivex.internal.subscribers.LambdaSubscriber
import org.reactivestreams.Subscriber

fun <T> Completable.toSender(): Sender<T> = toObservable<T>().toSender()
fun <T> Maybe<T>.toSender(): Sender<T> = toObservable().toSender()
fun <T> Single<T>.toSender(): Sender<T> = toObservable().toSender()

fun <T> Observable<T>.toSender(): Sender<T> =
    let { observable ->
        { receiver ->
            Consumer(receiver)
                .let { LambdaObserver<T>(it, ON_ERROR_MISSING, EMPTY_ACTION, emptyConsumer()) }
                .also(observable::subscribe)
                .run { (::dispose) }
        }
    }

fun <T> Flowable<T>.toSender(): Sender<T> =
    let { flowable ->
        { receiver ->
            Consumer(receiver)
                .let { LambdaSubscriber<T>(it, ON_ERROR_MISSING, EMPTY_ACTION, emptyConsumer()) }
                .also(flowable::subscribe)
                .run { (::dispose) }
        }
    }

fun <T> CompletableObserver.toReceiver(): Receiver<T> = { onComplete() }
fun <T> MaybeObserver<T>.toReceiver(): Receiver<T> = ::onSuccess
fun <T> SingleObserver<T>.toReceiver(): Receiver<T> = ::onSuccess
fun <T> Observer<T>.toReceiver(): Receiver<T> = ::onNext
fun <T> Subscriber<T>.toReceiver(): Receiver<T> = ::onNext