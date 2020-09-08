package dog.snow.androidrecruittest

import android.app.Application
import dog.snow.androidrecruittest.di.AppGraph
import dog.snow.androidrecruittest.log.MDebugTree
import timber.log.Timber

class MyApplication : Application() {

    var appGraph: AppGraph = AppGraph()
    override fun onCreate() {
        super.onCreate()
        Timber.plant(MDebugTree())
    }
}