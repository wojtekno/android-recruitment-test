package dog.snow.androidrecruittest.repository

import dog.snow.androidrecruittest.network.PlaceholderApi
import dog.snow.androidrecruittest.repository.model.RawAlbum
import dog.snow.androidrecruittest.repository.model.RawPhoto
import dog.snow.androidrecruittest.repository.model.RawUser
import dog.snow.androidrecruittest.scheduler.SchedulerProvider
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.BehaviorSubject

class PlaceholderRepositoryImpl(private val placeholderApi: PlaceholderApi, private val schedulerProvider: SchedulerProvider) : PlaceholderRepository {

    private var rawPhotosSubject: BehaviorSubject<List<RawPhoto>> = BehaviorSubject.create()
    private var rawAlbumSubject: BehaviorSubject<List<RawAlbum>> = BehaviorSubject.create()
    private var rawUserSubject: BehaviorSubject<List<RawUser>> = BehaviorSubject.create()

    init {
        placeholderApi.getPhotos()
                .retry(3)
                .map {
                    fetchAlbumInfo(it)
                    it
                }
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribeBy(
                        onError = {
                            rawPhotosSubject.onError(it)
//                            rawPhotosSubject = BehaviorSubject.create()
                        },
                        onNext = { rawPhotosSubject.onNext(it) }
                )
    }

    private fun fetchAlbumInfo(photos: List<RawPhoto>) {
        Observable.fromIterable(photos)
                .distinct { it.albumId }
                .map { it.albumId }
                .toList()
                .flatMapObservable { placeholderApi.getAlbums(it) }
                .retry(3)
                .map {
                    fetchUsersInfo(it)
                    it
                }
                .subscribeBy(
                        onError = {
                            rawAlbumSubject.onError(it)
//                            rawAlbumSubject = BehaviorSubject.create()
                        },
                        onNext =
                        { rawAlbumSubject.onNext(it) }
                )
    }

    private fun fetchUsersInfo(albums: List<RawAlbum>) {
        Observable.fromIterable(albums)
                .distinct { it.userId }
                .map { it.userId }
                .toList()
                .flatMapObservable { placeholderApi.getUsers(it) }
                .retry(3)
                .subscribeBy(
                        onError = {
                            rawUserSubject.onError(it)
//                            rawUserSubject = BehaviorSubject.create()
                        },
                        onNext = { rawUserSubject.onNext(it) }
                )
    }


    override fun getRawPhotos(): Observable<List<RawPhoto>> = rawPhotosSubject

    override fun getRawAlbums(): Observable<List<RawAlbum>> = rawAlbumSubject

    override fun getRawUsers(): Observable<List<RawUser>> = rawUserSubject

    override fun refetchPhotos(quantity: Int) {
        // TODO: let app using previously stored data if there was any, when refetching emits error
        placeholderApi.getPhotos(quantity)

                .map {
                    fetchAlbumInfo(it)
                    it
                }
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.mainThread())
                .subscribeBy(
                        onError = { rawPhotosSubject.onError(it) },
                        onNext = { rawPhotosSubject.onNext(it) }
                )
    }
}