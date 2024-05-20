package dev.wiskiw.spacexapp.app

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SpaceXApp : Application() {

    override fun onCreate() {
        super.onCreate()

        initDi()
    }

    private fun initDi() {
        startKoin {
            androidContext(this@SpaceXApp)
            allowOverride(false)

            modules(
                appModule,
                viewModelModule,
            )
        }
    }
}
