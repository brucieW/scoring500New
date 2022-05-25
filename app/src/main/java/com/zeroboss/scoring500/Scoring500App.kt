package com.zeroboss.scoring500

import android.app.Application
import com.zeroboss.scoring500.di.*
import io.objectbox.android.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.Level
import scoringRepositoryModule

class Scoring500App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@Scoring500App)
            modules(
                scoringRepositoryModule,
                boxStoreModule,
                useCasesModule,
                loginViewModelModule,
                homeViewModelModule,
                gameViewModelModule,
                newMatchViewModelModule,
                newHandViewModelModule,
                statisticsViewModelModule,
                scoringRulesViewModelModule
            )
        }
    }
}