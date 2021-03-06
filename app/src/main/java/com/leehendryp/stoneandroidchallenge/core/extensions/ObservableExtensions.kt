package com.leehendryp.stoneandroidchallenge.core.extensions

import hu.akarnokd.rxjava3.bridge.RxJavaBridge
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

// FIXME Lee April 27, 2020: as of now, there's not Room integration with RxJava3
// This project uses Akarnokd's bridge lib for doing so for now. Akarnokd is one of the
// people behind RxJava. Below, as() methods are used to make the proper conversions

fun io.reactivex.Completable.asV3Completable(): Completable =
    `as`(RxJavaBridge.toV3Completable())

fun <T> io.reactivex.Flowable<T>.asV3Flowable(): Flowable<T> =
    `as`(RxJavaBridge.toV3Flowable())

fun <T : Any> Observable<out T>.subscribeOnIO(): Observable<out T> {
    return subscribeOn(Schedulers.io())
}

fun <T : Any> Observable<out T>.observeOnMain(): Observable<out T> {
    return observeOn(AndroidSchedulers.mainThread())
}

fun Completable.subscribeOnIO(): Completable =
    subscribeOn(Schedulers.io())

fun <T : Any> Flowable<out T>.subscribeOnIO(): Flowable<out T> =
    subscribeOn(Schedulers.io())

fun <T : Any> Flowable<out T>.observeOnMain(): Flowable<out T> =
    observeOn(AndroidSchedulers.mainThread())
