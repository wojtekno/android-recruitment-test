package dog.snow.androidrecruittest.usecases

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import dog.snow.androidrecruittest.R
import dog.snow.androidrecruittest.repository.PlaceholderRepository
import dog.snow.androidrecruittest.testutils.createListItems
import dog.snow.androidrecruittest.testutils.createRawAlbumList
import dog.snow.androidrecruittest.testutils.createRawPhotoList
import dog.snow.androidrecruittest.testutils.createRawUserList
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Test

class GetListItemsUseCaseImplTest {

    @Test
    fun `given repository returns rawPhotoList and rawAlbumList when initializing then listItem emits list`() {
        val photoList = createRawPhotoList(100, 5)
        val albumList = createRawAlbumList(5, 2)
        val userList = createRawUserList(2)
        val itemList = createListItems(100, 5, false, 2)
        val repoMock: PlaceholderRepository = mock {
            on { getRawPhotos() } doReturn Observable.just(photoList)
            on { getRawAlbums() } doReturn Observable.just(albumList)
        }
        val useCase = createUseCase(repo = repoMock)

        useCase.getListItems().test()
                .assertValue(itemList)
                .dispose()
    }

    @Test
    fun `given repository returns error when getRawPhotos() then listItem emits that error`() {
        val throwable = Throwable("error")
        val repoMock: PlaceholderRepository = mock {
            on { getRawPhotos() } doReturn Observable.error(throwable)
            on { getRawAlbums() } doReturn Observable.never()
        }
        val useCase = createUseCase(repo = repoMock)

        useCase.getListItems().test()
                .assertError(throwable)
                .dispose()
    }

    @Test
    fun `given repository returns rawPhotoList and error when getRawAlbums() then listItem emits incomplete list`() {
        val throwable = Throwable("error")
        val photoList = createRawPhotoList(100, 5)
        val itemList = createListItems(100, 5, true, 2)

        val repoMock: PlaceholderRepository = mock {
            on { getRawPhotos() } doReturn Observable.just(photoList)
            on { getRawAlbums() } doReturn Observable.error(throwable)
        }
        val useCase = createUseCase(repo = repoMock)

        useCase.getListItems().test()
                .assertValue(itemList)
                .dispose()
    }


    private fun createUseCase(
            repo: PlaceholderRepository = mock()): GetListItemsUseCaseImpl {
        return GetListItemsUseCaseImpl(repo, schedulers = mock {
            on { io() } doReturn Schedulers.trampoline()
            on { mainThread() } doReturn Schedulers.trampoline()
        }, resourceProvider = mock {
            on { getString(R.string.cant_download_album_id_message) } doReturn "Album id: %s - Can\'t download necessary data!"
        }
        )
    }
}