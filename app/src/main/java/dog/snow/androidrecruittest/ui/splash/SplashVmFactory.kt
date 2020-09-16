package dog.snow.androidrecruittest.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dog.snow.androidrecruittest.resourceprovider.ResourceProvider
import dog.snow.androidrecruittest.scheduler.SchedulerProvider
import dog.snow.androidrecruittest.usecases.FetchDataUseCase

class SplashVmFactory(private val fetchUseCase: FetchDataUseCase, private val schedulers: SchedulerProvider, private val resourceProvider: ResourceProvider) : ViewModelProvider.NewInstanceFactory() {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SplashViewModel(fetchUseCase, schedulers, resourceProvider) as T
    }
}