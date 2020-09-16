package dog.snow.androidrecruittest.usecases

import dog.snow.androidrecruittest.ui.model.Detail
import io.reactivex.rxjava3.core.Observable

interface GetDetailUseCase {
    fun getItemDetail(listId: Int): Observable<Detail>
    fun getItemDetailById(itemId: Int): Observable<Detail>
}