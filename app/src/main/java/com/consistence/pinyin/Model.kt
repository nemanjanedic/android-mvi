package com.consistence.pinyin

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.content.Context
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

abstract class Model<I : ViewIntent, S : ViewState>(application: Application) : AndroidViewModel(application) {

    val intents: PublishSubject<I> = PublishSubject.create<I>()

    private val disposable = CompositeDisposable()

    internal fun d(disposable: Disposable) {
        this.disposable.add(disposable)
    }

    internal fun <T> observable(item: T) = Observable.just(item)

    internal fun states(): Observable<S> = intents
            .flatMap({ reducer(it) })

    internal fun publish(intent: I) {
        intents.onNext(intent)
    }

    protected fun context(): Context = getApplication()

    abstract fun reducer(intent: I): Observable<S>
}