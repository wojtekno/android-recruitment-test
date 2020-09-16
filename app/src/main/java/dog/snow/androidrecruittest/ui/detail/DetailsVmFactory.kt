package dog.snow.androidrecruittest.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dog.snow.androidrecruittest.scheduler.SchedulerProvider
import dog.snow.androidrecruittest.ui.model.ListItem
import dog.snow.androidrecruittest.usecases.GetDetailUseCase

class DetailsVmFactory(private val detailsUseCase: GetDetailUseCase, private val schedulers: SchedulerProvider) : ViewModelProvider.NewInstanceFactory() {
    private lateinit var listItem: Pair<Int, ListItem>
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DetailsViewModel(listItem, detailsUseCase, schedulers) as T
    }

    fun setListItem(listItem: Pair<Int, ListItem>) {
        this.listItem = listItem
    }
}