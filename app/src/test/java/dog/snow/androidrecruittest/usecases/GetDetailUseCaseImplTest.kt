package dog.snow.androidrecruittest.usecases

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import dog.snow.androidrecruittest.R
import dog.snow.androidrecruittest.repository.PlaceholderRepository
import dog.snow.androidrecruittest.resourceprovider.ResourceProvider
import dog.snow.androidrecruittest.testutils.createDetailList
import dog.snow.androidrecruittest.testutils.createRawAlbumList
import dog.snow.androidrecruittest.testutils.createRawPhotoList
import dog.snow.androidrecruittest.testutils.createRawUserList
import io.reactivex.rxjava3.core.Observable
import org.junit.Test

class GetDetailUseCaseImplTest {

    @Test
    fun `given repository returns error when getRawPhotos() then getItemDetail() returns error`() {
        val throwableMock = Throwable("error")
        val useCase = createUseCase(repo = mock {
            on { getRawPhotos() } doReturn Observable.error(throwableMock)
            on { getRawAlbums() } doReturn Observable.never()
            on { getRawUsers() } doReturn Observable.never()
        })

        useCase.getItemDetail(2).test()
                .assertError(throwableMock)
                .dispose()
    }

    @Test
    fun `given repository returns rawPhotoList and error on getRawAlbums() when initializing then getItemDetail() returns incomplete Detail`() {
        val throwableMock = Throwable("error")
        val throwableMock2 = Throwable("error2")
        val photoList = createRawPhotoList(200, 17)
        val detailList = createDetailList(200, 17, albumsError = true, usersError = true)
        val useCase = createUseCase(repo = mock {
            on { getRawPhotos() } doReturn Observable.just(photoList)
            on { getRawAlbums() } doReturn Observable.error(throwableMock)
            on { getRawUsers() } doReturn Observable.error(throwableMock2)
        })

        useCase.getItemDetail(2).test()
                .assertValue(detailList[2])
                .dispose()
    }

    @Test
    fun `given repository returns rawPhotoList, rawAlbumList and error on getRawUsers() when initializing then getItemDetail() returns incomplete Detail`() {
        val throwableMock = Throwable("error")
        val photoList = createRawPhotoList(200, 17)
        val albumList = createRawAlbumList(17, 12)
        val detailList = createDetailList(200, 17, users = 12, usersError = true)
        val useCase = createUseCase(repo = mock {
            on { getRawPhotos() } doReturn Observable.just(photoList)
            on { getRawAlbums() } doReturn Observable.just(albumList)
            on { getRawUsers() } doReturn Observable.error(throwableMock)
        })

        useCase.getItemDetail(2).test()
                .assertValue(detailList[2])
                .dispose()
    }

    @Test
    fun `given repository returns rawPhotoList, rawAlbumList and rawUserList when initializing then getItemDetail() returns complete Detail list`() {
        val throwableMock = Throwable("error")
        val photoList = createRawPhotoList(200, 17)
        val albumList = createRawAlbumList(17, 12)
        val userList = createRawUserList(12)
        val detailList = createDetailList(200, 17, users = 12)
        val useCase = createUseCase(repo = mock {
            on { getRawPhotos() } doReturn Observable.just(photoList)
            on { getRawAlbums() } doReturn Observable.just(albumList)
            on { getRawUsers() } doReturn Observable.just(userList)
        })

        useCase.getItemDetail(199).test()
                .assertValue(detailList[199])
                .dispose()
    }

    private fun createUseCase(
            repo: PlaceholderRepository = mock(),
            resourceProvider: ResourceProvider = mock {
                on { getString(R.string.cant_download_album_id_message) } doReturn "Album id: %s - Can\'t download necessary data!"
                on { getString(R.string.cant_download_user_id_message) } doReturn "User id: %s - Can\'t download necessary data!"
                on { getString(R.string.cant_download_general_message) } doReturn "Can\'t download necessary data!"
            })
            : GetDetailUseCase {

        return GetDetailUseCaseImpl(repo, resourceProvider)
    }
}