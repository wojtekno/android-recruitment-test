package dog.snow.androidrecruittest.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dog.snow.androidrecruittest.scheduler.SchedulerProvider
import dog.snow.androidrecruittest.ui.model.Detail
import dog.snow.androidrecruittest.ui.model.ListItem
import dog.snow.androidrecruittest.usecases.GetDetailUseCase
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import timber.log.Timber.d

class DetailsViewModel(clickedItem: Pair<Int, ListItem>, detailsUseCase: GetDetailUseCase, schedulers: SchedulerProvider) : ViewModel() {
    private val details = MutableLiveData<Detail>(Detail(clickedItem.second.id, clickedItem.second.title, clickedItem.second.albumTitle))
    private val disposables = CompositeDisposable()

    init {
        detailsUseCase.getItemDetailById(clickedItem.first)
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.io())
                .subscribeBy(
                        onError = { d(it.message) },
                        onNext = {
                            details.postValue(it)
                        }
                )
                .addTo(disposables)
    }

    fun getDetails(): LiveData<Detail> = details

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}