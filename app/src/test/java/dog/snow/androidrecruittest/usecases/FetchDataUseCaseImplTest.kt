package dog.snow.androidrecruittest.usecases

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import dog.snow.androidrecruittest.repository.PlaceholderRepository
import dog.snow.androidrecruittest.testutils.createRawAlbumList
import dog.snow.androidrecruittest.testutils.createRawPhotoList
import dog.snow.androidrecruittest.testutils.createRawUserList
import io.reactivex.rxjava3.core.Observable
import org.junit.Test

class FetchDataUseCaseImplTest {

    @Test
    fun `given repository hasn't returned anything yet when initializing then fetchingDataEnded doesn't emit `() {
        val throwableMock = Throwable("error")
        val repo: PlaceholderRepository = mock {
            on { getRawPhotos() } doReturn Observable.never()
            on { getRawAlbums() } doReturn Observable.never()
            on { getRawUsers() } doReturn Observable.never()
        }
        val useCase = createUseCase(repo)

        useCase.fetchingDataEnded().test()
                .assertNotComplete()
                .assertNoValues()
                .assertNoErrors()
                .dispose()
    }

    @Test
    fun `given repository returns error on getRawPhotos() when initializing then fetchingDataEnded emits that error `() {
        val throwableMock = Throwable("error")
        val repo: PlaceholderRepository = mock {
            on { getRawPhotos() } doReturn Observable.error(throwableMock)
            on { getRawAlbums() } doReturn Observable.never()
            on { getRawUsers() } doReturn Observable.never()
        }
        val useCase = createUseCase(repo)

        useCase.fetchingDataEnded().test()
                .assertError(throwableMock)
                .dispose()
    }

    @Test
    fun `given repository returns rawPhotoList but other calls haven't emitted yet when initializing then fetchingDataEnded doesn't emit `() {
        val throwableMock = Throwable("error")
        val photoList = createRawPhotoList(150, 33)
        val repo: PlaceholderRepository = mock {
            on { getRawPhotos() } doReturn Observable.just(photoList)
            on { getRawAlbums() } doReturn Observable.never()
            on { getRawUsers() } doReturn Observable.never()
        }
        val useCase = createUseCase(repo)

        useCase.fetchingDataEnded().test()
                .assertNotComplete()
                .assertNoValues()
                .dispose()
    }

    @Test
    fun `given repository returns rawPhotoList and error on getRawAlbums() when initializing then fetchingDataEnded emits 1 `() {
        val throwableMock = Throwable("error")
        val photoList = createRawPhotoList(150, 33)
        val repo: PlaceholderRepository = mock {
            on { getRawPhotos() } doReturn Observable.just(photoList)
            on { getRawAlbums() } doReturn Observable.error(throwableMock)
            on { getRawUsers() } doReturn Observable.never()
        }
        val useCase = createUseCase(repo)

        useCase.fetchingDataEnded().test()
                .assertValue(1)
                .assertComplete()
                .dispose()
    }


    @Test
    fun `given repository returns rawPhotoList, rawAlbumList and other calls hasn't emitted yet when initializing then fetchingDataEnded doesn't emit `() {
        val throwableMock = Throwable("error")
        val photoList = createRawPhotoList(150, 33)
        val albumList = createRawAlbumList(33, 11)
        val repo: PlaceholderRepository = mock {
            on { getRawPhotos() } doReturn Observable.just(photoList)
            on { getRawAlbums() } doReturn Observable.just(albumList)
            on { getRawUsers() } doReturn Observable.never()
        }
        val useCase = createUseCase(repo)

        useCase.fetchingDataEnded().test()
                .assertNoValues()
                .assertNotComplete()
                .dispose()
    }

    @Test
    fun `given repository returns rawPhotoList, rawAlbumList and error on getRawUsers() when initializing then fetchingDataEnded emits 2 `() {
        val throwableMock = Throwable("error")
        val photoList = createRawPhotoList(150, 33)
        val albumList = createRawAlbumList(33, 11)
        val repo: PlaceholderRepository = mock {
            on { getRawPhotos() } doReturn Observable.just(photoList)
            on { getRawAlbums() } doReturn Observable.just(albumList)
            on { getRawUsers() } doReturn Observable.error(throwableMock)
        }
        val useCase = createUseCase(repo)

        useCase.fetchingDataEnded().test()
                .assertValue(2)
                .assertComplete()
                .dispose()
    }

    @Test
    fun `given repository returns rawPhotoList, rawAlbumList and rawUserList when initializing then fetchingDataEnded emits 3 `() {
        val throwableMock = Throwable("error")
        val photoList = createRawPhotoList(150, 33)
        val albumList = createRawAlbumList(33, 11)
        val userList = createRawUserList(11)
        val repo: PlaceholderRepository = mock {
            on { getRawPhotos() } doReturn Observable.just(photoList)
            on { getRawAlbums() } doReturn Observable.just(albumList)
            on { getRawUsers() } doReturn Observable.just(userList)
        }
        val useCase = createUseCase(repo)

        useCase.fetchingDataEnded().test()
                .assertValue(3)
                .assertComplete()
                .dispose()
    }

    private fun createUseCase(repository: PlaceholderRepository): FetchDataUseCase {
        return FetchDataUseCaseImpl(repository)
    }
}