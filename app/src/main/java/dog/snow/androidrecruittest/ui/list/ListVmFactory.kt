package dog.snow.androidrecruittest.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dog.snow.androidrecruittest.resourceprovider.ResourceProvider
import dog.snow.androidrecruittest.scheduler.SchedulerProvider
import dog.snow.androidrecruittest.usecases.GetListItemsUseCase

class ListVmFactory(private val listItemsUseCase: GetListItemsUseCase, private val schedulers: SchedulerProvider, private val resourceProvider: ResourceProvider) : ViewModelProvider.NewInstanceFactory() {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ListViewModel(listItemsUseCase, schedulers) as T
    }
}