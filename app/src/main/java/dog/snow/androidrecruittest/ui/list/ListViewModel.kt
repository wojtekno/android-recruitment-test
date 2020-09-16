package dog.snow.androidrecruittest.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dog.snow.androidrecruittest.scheduler.SchedulerProvider
import dog.snow.androidrecruittest.ui.model.ListItem
import dog.snow.androidrecruittest.usecases.GetListItemsUseCase
import io.reactivex.rxjava3.kotlin.subscribeBy

class ListViewModel(listItemsUseCase: GetListItemsUseCase, schedulers: SchedulerProvider) : ViewModel() {

    private val listItemsLd = MutableLiveData<List<ListItem>>()

    init {
        listItemsUseCase.getListItems()
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.mainThread())
                .subscribeBy(
                        onNext = { listItemsLd.value = it }
                )
    }

    fun listItemsLd(): LiveData<List<ListItem>> = listItemsLd

}