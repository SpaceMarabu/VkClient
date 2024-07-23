package com.example.vkclient.extensions

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.merge

fun <T> Flow<T>.mergeWith(another: Flow<T>): Flow<T> {
    return merge(this, another)
}

fun ComponentContext.componentScope() =
//выполнение немедленно на основном потоке (UI, чтобы предотвратить мерцание)
    // + SupervisorJob предотвращает отмену дочерних корутин, если одна из них упадет
    CoroutineScope(Dispatchers.Main.immediate + SupervisorJob())
        .apply {
            lifecycle.doOnDestroy { cancel() }
        }
