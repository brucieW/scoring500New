package com.zeroboss.scoring500.di

import android.content.Context
import android.util.Log
import com.zeroboss.scoring500.domain.model.MyObjectBox
import io.objectbox.BoxStore
import io.objectbox.DebugFlags
import io.objectbox.android.AndroidObjectBrowser
import io.objectbox.android.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import java.io.File

val boxStoreModule = module {
    single { provideBoxStore(androidContext()) }
}

const val liveFile = "scoring500"
val testFile = File("test-db")

lateinit var boxStore: BoxStore

val boxStoreTestModule = module {
    single { provideTestBoxStore() }
}

fun provideBoxStore(
    context: Context
) : BoxStore {
    val boxStore = MyObjectBox
        .builder()
        .name(liveFile)
        .androidContext(context.applicationContext)
        .build()

    if (BuildConfig.DEBUG) {
        Log.i("Object Browser", "Starting")
        val started = AndroidObjectBrowser(boxStore).start(context)
        Log .i("Object Browser", "Started: " + started)
    }

    return boxStore
}

private fun provideTestBoxStore(): BoxStore {
    return MyObjectBox
        .builder()
        .directory(testFile)
        .debugFlags(DebugFlags.LOG_QUERIES + DebugFlags.LOG_QUERY_PARAMETERS)
        .build()
}

