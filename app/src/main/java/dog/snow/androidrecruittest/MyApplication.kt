package dog.snow.androidrecruittest

import android.app.Application
import dog.snow.androidrecruittest.di.AppGraph
import dog.snow.androidrecruittest.log.MDebugTree
import timber.log.Timber

class MyApplication : Application() {

    lateinit var appGraph: AppGraph
    override fun onCreate() {
        super.onCreate()
        Timber.plant(MDebugTree())
        appGraph = AppGraph(applicationContext)
    }
}