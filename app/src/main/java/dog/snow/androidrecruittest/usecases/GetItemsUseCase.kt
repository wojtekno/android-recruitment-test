package dog.snow.androidrecruittest.usecases

import dog.snow.androidrecruittest.repository.PlaceholderRepository
import dog.snow.androidrecruittest.repository.model.RawAlbum
import dog.snow.androidrecruittest.repository.model.RawPhoto
import dog.snow.androidrecruittest.repository.model.RawUser
import dog.snow.androidrecruittest.scheduler.SchedulerProvider
import dog.snow.androidrecruittest.ui.model.ListItem
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.BehaviorSubject
import org.jetbrains.annotations.TestOnly
import timber.log.Timber.d

class GetItemsUseCase(private val repository: PlaceholderRepository, private val schedulers: SchedulerProvider) {

    val listItems: BehaviorSubject<List<ListItem>> = BehaviorSubject.create()
    private val photosSubject: BehaviorSubject<List<RawPhoto>> = BehaviorSubject.create()
    private val albumsSubject: BehaviorSubject<List<RawAlbum>> = BehaviorSubject.create()
    private val usersSubject: BehaviorSubject<List<RawUser>> = BehaviorSubject.create()

    @TestOnly
    fun photosSubject(): Observable<List<RawPhoto>> = photosSubject
    @TestOnly
    fun albumsSubject(): Observable<List<RawAlbum>> = albumsSubject
    @TestOnly
    fun usersSubject(): Observable<List<RawUser>> = usersSubject

    init {
        repository.getPhotos()
                .subscribeBy(
                        onError = { photosSubject.onError(it) },
                        onNext = { photosSubject.onNext(it) }
                )

        photosSubject
                .map { list -> list.distinctBy { it.albumId }.map { it.albumId }.toList() }
                .flatMap {
                    d("thread  ${Thread.currentThread().name}")
                    repository.getAlbums(it) }
                .subscribeBy(
                        onNext = { albumsSubject.onNext(it) },
                        onError = { albumsSubject.onError(it) }
                )

        albumsSubject
                .map { list -> list.distinctBy { it.userId }.map { it.userId }.toList() }
                .flatMap { repository.getUsers(it) }
                .subscribeBy(
                        onError = { usersSubject.onError(it) },
                        onNext = { usersSubject.onNext(it) }
                )

        Observable.zip(photosSubject, albumsSubject, BiFunction<List<RawPhoto>, List<RawAlbum>, List<ListItem>> { photos, albums ->
            val items = mutableListOf<ListItem>()
            for (photo in photos) {
                val album = albums.first { it.id == photo.albumId }
                val item = ListItem(photo.id, photo.title, album.title, photo.thumbnailUrl)
                items.add(item)
            }
            return@BiFunction items
        })
                .subscribeBy(
                        onError = { listItems.onError(it) },
                        onNext = { listItems.onNext(it) }
                )
    }


}