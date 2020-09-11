package dog.snow.androidrecruittest.usecases

import dog.snow.androidrecruittest.ui.model.ListItem
import io.reactivex.rxjava3.core.Observable

interface GetListItemsUseCase {
    fun getListItems(): Observable<List<ListItem>>

    /**
     * for development testing purposes only
     */
    fun refetch()
}