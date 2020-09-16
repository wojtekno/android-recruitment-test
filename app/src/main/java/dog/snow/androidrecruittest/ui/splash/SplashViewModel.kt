package dog.snow.androidrecruittest.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dog.snow.androidrecruittest.R
import dog.snow.androidrecruittest.resourceprovider.ResourceProvider
import dog.snow.androidrecruittest.scheduler.SchedulerProvider
import dog.snow.androidrecruittest.usecases.FetchDataUseCase
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy

class SplashViewModel(private val fetchUseCase: FetchDataUseCase, private val schedulers: SchedulerProvider, private val resourceProvider: ResourceProvider) : ViewModel() {

    private val downloadingError = MutableLiveData<String?>()
    private val downloadingCompletion = MutableLiveData<String>()
    private val disposables = CompositeDisposable()

    init {
        subscribeToFetchingDataResult()
    }

    private fun subscribeToFetchingDataResult() {
        fetchUseCase.fetchingDataEnded()
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.mainThread())
                .subscribeBy(
                        onError = {
                            downloadingError.value = it.message
                        },
                        onSuccess = {
                            downloadingCompletion.value = when (it) {
                                3 -> resourceProvider.getString(R.string.downloading_successful)
                                else -> resourceProvider.getString(R.string.data_downloaded_partially_message)
                            }
                        }
                )
                .addTo(disposables)
    }

    fun downloadingCompletion(): LiveData<String> = downloadingCompletion

    fun downloadingError(): LiveData<String?> = downloadingError
    fun retry() {
        disposables.clear()
        fetchUseCase.refetch()
        subscribeToFetchingDataResult()
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

}