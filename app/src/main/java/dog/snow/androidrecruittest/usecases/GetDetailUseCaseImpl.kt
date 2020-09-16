package dog.snow.androidrecruittest.usecases

import dog.snow.androidrecruittest.R
import dog.snow.androidrecruittest.repository.PlaceholderRepository
import dog.snow.androidrecruittest.repository.model.RawAlbum
import dog.snow.androidrecruittest.repository.model.RawPhoto
import dog.snow.androidrecruittest.repository.model.RawUser
import dog.snow.androidrecruittest.resourceprovider.ResourceProvider
import dog.snow.androidrecruittest.ui.model.Detail
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.functions.Function3
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.BehaviorSubject
import okhttp3.internal.format

class GetDetailUseCaseImpl(private val repository: PlaceholderRepository, private val resourceProvider: ResourceProvider) : GetDetailUseCase {

    private val detailListSubject: BehaviorSubject<List<Detail>> = BehaviorSubject.create()

    init {
        prepareDataAndSubscribe()
    }

    override fun getItemDetail(listId: Int): Observable<Detail> = detailListSubject.map { it[listId] }

    override fun getItemDetailById(itemId: Int): Observable<Detail> = detailListSubject.map { list -> list.first { it.photoId == itemId } }


    private fun prepareDataAndSubscribe() {
        val (rawAlbums, rawUsers) = prepareRawSubjects()

        Observable.zip(repository.getRawPhotos(), rawAlbums, rawUsers, zipPhotosAlbumsAndUsersToDetailList())
                .subscribeBy(
                        onError = { detailListSubject.onError(it) },
                        onNext = { detailListSubject.onNext(it) }
                )
    }

    private fun prepareRawSubjects(): Pair<BehaviorSubject<List<RawAlbum>>, BehaviorSubject<List<RawUser>>> {
        val rawAlbums = BehaviorSubject.create<List<RawAlbum>>()
        repository.getRawAlbums()
                .subscribeBy(
                        onError = { rawAlbums.onNext(emptyList()) },
                        onNext = { rawAlbums.onNext(it) }
                )

        val rawUsers = BehaviorSubject.create<List<RawUser>>()
        repository.getRawUsers()
                .subscribeBy(
                        onError = { rawUsers.onNext(emptyList()) },
                        onNext = { rawUsers.onNext(it) }
                )
        return Pair(rawAlbums, rawUsers)
    }


    private fun zipPhotosAlbumsAndUsersToDetailList(): Function3<List<RawPhoto>, List<RawAlbum>, List<RawUser>, List<Detail>> {
        return Function3<List<RawPhoto>, List<RawAlbum>, List<RawUser>, List<Detail>> { photos, albums, users ->
            val list = mutableListOf<Detail>()
            for (photo in photos) {
                val album: RawAlbum? = albums.firstOrNull { it.id == photo.albumId }
                var albumTitle: String
                var userInfo: Array<String> = Array(3) { "" }

                if (album == null) {
                    albumTitle = format(resourceProvider.getString(R.string.cant_download_album_id_message), photo.albumId)
                    userInfo = Array(3) { resourceProvider.getString(R.string.cant_download_general_message) }
                } else {
                    albumTitle = album.title
                    val user: RawUser? = users.firstOrNull { it.id == album.userId }
                    if (user == null) {
                        userInfo = Array(3) { format(resourceProvider.getString(R.string.cant_download_user_id_message), album.userId) }
                    } else {
                        userInfo[0] = user.username
                        userInfo[1] = user.email
                        userInfo[2] = user.phone
                    }
                }
                val item = Detail(photo.id, photo.title, albumTitle, userInfo[0], userInfo[1], userInfo[2], photo.url)
                list.add(item)
            }
            return@Function3 list
        }
    }
}