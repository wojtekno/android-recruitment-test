package dog.snow.androidrecruittest.repository

import dog.snow.androidrecruittest.network.PlaceholderApi
import dog.snow.androidrecruittest.repository.model.RawAlbum
import dog.snow.androidrecruittest.repository.model.RawPhoto
import dog.snow.androidrecruittest.repository.model.RawUser
import dog.snow.androidrecruittest.scheduler.SchedulerProvider
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import timber.log.Timber.d


class PlaceholderRepositoryImpl(private val placeholderApi: PlaceholderApi, schedulerProvider: SchedulerProvider) : PlaceholderRepository {

    override fun getPhotos(quantity: Int): Observable<List<RawPhoto>> {
        d("getPhotos $quantity")
        return placeholderApi.getPhotos(quantity).subscribeOn(Schedulers.io())
    }

    override fun getAlbums(albumIds: List<Int>): Observable<List<RawAlbum>> {
        d("getAlbums ${albumIds.size}")
        return placeholderApi.getAlbums(albumIds)
    }

    override fun getUsers(userIds: List<Int>): Observable<List<RawUser>> {
        d("geUsers ${userIds.size}")
        return placeholderApi.getUsers(userIds)
    }

}


