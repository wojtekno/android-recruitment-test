package dog.snow.androidrecruittest.usecases

import dog.snow.androidrecruittest.repository.PlaceholderRepository
import dog.snow.androidrecruittest.repository.model.RawAlbum
import dog.snow.androidrecruittest.repository.model.RawPhoto
import dog.snow.androidrecruittest.scheduler.SchedulerProvider
import dog.snow.androidrecruittest.ui.model.ListItem
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.BehaviorSubject
import timber.log.Timber.d

class GetListItemsUseCaseImpl(private val repository: PlaceholderRepository, private val schedulers: SchedulerProvider) : GetListItemsUseCase {

    private val listItems: BehaviorSubject<List<ListItem>> = BehaviorSubject.create()

    init {
        Observable.zip(repository.getRawPhotos(), repository.getRawAlbums(),
                BiFunction<List<RawPhoto>, List<RawAlbum>, List<ListItem>> { photos, albums ->
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

    override fun refetch() {
        d("refetching")
        repository.refetchPhotos(200)
    }

    override fun getListItems(): Observable<List<ListItem>> = listItems


}