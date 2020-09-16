package dog.snow.androidrecruittest.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dog.snow.androidrecruittest.R
import dog.snow.androidrecruittest.resourceprovider.ResourceProvider
import dog.snow.androidrecruittest.scheduler.SchedulerProvider
import dog.snow.androidrecruittest.usecases.FetchDataUseCase
import io.reactivex.rxjava3.kotlin.subscribeBy
import java.util.concurrent.TimeUnit

class SplashViewModel(private val fetchUseCase: FetchDataUseCase, schedulers: SchedulerProvider, resourceProvider: ResourceProvider) : ViewModel() {

    private val downloadingError = MutableLiveData<String?>()
    private val downloadingCompletion = MutableLiveData<String>()

    init {
        fetchUseCase.fetchingDataEnded()
                .delay(1000, TimeUnit.MILLISECONDS)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.mainThread())
                .subscribeBy(
                        onError = { downloadingError.value = it.message },
                        onSuccess = {
                            downloadingCompletion.value = when (it) {
                                3 -> resourceProvider.getString(R.string.downloading_successful)
                                else -> resourceProvider.getString(R.string.data_downloaded_partially_message)
                            }
                        }
                )
    }

    fun downloadingCompletion(): LiveData<String> = downloadingCompletion

    fun downloadingError(): LiveData<String?> = downloadingError
    fun retry() {
        fetchUseCase.refetch()
    }

}