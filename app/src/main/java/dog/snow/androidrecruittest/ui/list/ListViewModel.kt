package dog.snow.androidrecruittest.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dog.snow.androidrecruittest.scheduler.SchedulerProvider
import dog.snow.androidrecruittest.ui.model.ListItem
import dog.snow.androidrecruittest.usecases.GetListItemsUseCase
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.BehaviorSubject
import timber.log.Timber.d

class ListViewModel(listItemsUseCase: GetListItemsUseCase, private val schedulers: SchedulerProvider) : ViewModel() {

    private lateinit var immutableList: List<ListItem>
    private val listItemsLd = MutableLiveData<List<ListItem>>()
    private val searchingSubject = BehaviorSubject.create<String>()

    init {
        listItemsUseCase.getListItems()
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            listItemsLd.value = it
                            immutableList = it
                        }
                )

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
                        onNext = {
                            d("onnext size ${it.size}")
                            listItemsLd.postValue(it)
                        },
                        onError = { d(it) })

    }

    fun search(phrase: String) {
        searchingSubject.onNext(phrase)
    }

    fun listItemsLd(): LiveData<List<ListItem>> = listItemsLd

}