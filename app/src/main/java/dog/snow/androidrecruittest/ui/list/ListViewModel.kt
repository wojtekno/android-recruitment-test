package dog.snow.androidrecruittest.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dog.snow.androidrecruittest.scheduler.SchedulerProvider
import dog.snow.androidrecruittest.ui.model.ListItem
import dog.snow.androidrecruittest.usecases.GetListItemsUseCase
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.BehaviorSubject

class ListViewModel(listItemsUseCase: GetListItemsUseCase, private val schedulers: SchedulerProvider) : ViewModel() {

    private lateinit var immutableList: List<ListItem>
    private val listItemsLd = MutableLiveData<List<ListItem>>()
    private val searchingSubject = BehaviorSubject.create<String>()
    private val disposables = CompositeDisposable()


    init {
        listItemsUseCase.getListItems()
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            listItemsLd.value = it
                            immutableList = it
                        },
                        onError = {}
                )
                .addTo(disposables)

        searchingSubject
                .map { phrase ->
                    immutableList.filter {
                        it.title.toLowerCase().contains(phrase.toLowerCase()) ||
                                it.albumTitle.toLowerCase().contains(phrase.toLowerCase())
                    }
                }
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.mainThread())
                .subscribeBy(
                        onNext = { listItemsLd.postValue(it) },
                        onError = {})
                .addTo(disposables)

    }

    fun search(phrase: String) {
        searchingSubject.onNext(phrase)
    }

    fun listItemsLd(): LiveData<List<ListItem>> = listItemsLd

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

}