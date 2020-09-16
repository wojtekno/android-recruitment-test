package dog.snow.androidrecruittest.usecases

import dog.snow.androidrecruittest.R
import dog.snow.androidrecruittest.repository.PlaceholderRepository
import dog.snow.androidrecruittest.repository.model.RawAlbum
import dog.snow.androidrecruittest.repository.model.RawPhoto
import dog.snow.androidrecruittest.resourceprovider.ResourceProvider
import dog.snow.androidrecruittest.scheduler.SchedulerProvider
import dog.snow.androidrecruittest.ui.model.ListItem
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.BehaviorSubject
import okhttp3.internal.format

class GetListItemsUseCaseImpl(private val repository: PlaceholderRepository, private val schedulers: SchedulerProvider, private val resourceProvider: ResourceProvider) : GetListItemsUseCase {

    private val listItems: BehaviorSubject<List<ListItem>> = BehaviorSubject.create()

    init {
        getDataAndSubscribe()
    }

    private fun getDataAndSubscribe() {
        val rawAlbums = prepareRawAlbums()
        zipAndCreateListItems(rawAlbums)
                .subscribeBy(
                        onError = { },
                        onNext = { listItems.onNext(it) }
                )
    }


    private fun zipAndCreateListItems(rawAlbums: BehaviorSubject<List<RawAlbum>>): @NonNull Observable<List<ListItem>> {
        return Observable.zip(repository.getRawPhotos(), rawAlbums,
                BiFunction<List<RawPhoto>, List<RawAlbum>, List<ListItem>> { photos, albums ->
                    val items = mutableListOf<ListItem>()
                    for (photo in photos) {
                        val album: RawAlbum? = albums.firstOrNull { it.id == photo.albumId }
                        val albumTitle = album?.title
                                ?: format(resourceProvider.getString(R.string.cant_download_album_id_message), photo.albumId)
                        val item = ListItem(photo.id, photo.title, albumTitle, photo.thumbnailUrl)
                        items.add(item)
                    }
                    return@BiFunction items
                })
    }

    private fun prepareRawAlbums(): BehaviorSubject<List<RawAlbum>> {
        val rawAlbums = BehaviorSubject.create<List<RawAlbum>>()
        repository.getRawAlbums()
                .subscribeBy(
                        onNext = {
                            rawAlbums.onNext(it)
                        },
                        onError = { rawAlbums.onNext(emptyList()) }
                )
        return rawAlbums
    }

    override fun getListItems(): Observable<List<ListItem>> = listItems


}
