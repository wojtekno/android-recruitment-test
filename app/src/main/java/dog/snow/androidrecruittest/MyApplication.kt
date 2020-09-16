package dog.snow.androidrecruittest

import android.app.Application
import dog.snow.androidrecruittest.di.AppGraph
import dog.snow.androidrecruittest.log.MDebugTree
import timber.log.Timber
import timber.log.Timber.d

class MyApplication : Application() {

    lateinit var appGraph: AppGraph
    override fun onCreate() {
        super.onCreate()
        Timber.plant(MDebugTree())
        d("MyApplication onCreate()")
        appGraph = AppGraph(applicationContext)
    }
}