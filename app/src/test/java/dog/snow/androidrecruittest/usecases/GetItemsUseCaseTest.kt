package dog.snow.androidrecruittest.usecases

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import dog.snow.androidrecruittest.repository.PlaceholderRepository
import dog.snow.androidrecruittest.testutils.createListItems
import dog.snow.androidrecruittest.testutils.createRawAlbumList
import dog.snow.androidrecruittest.testutils.createRawPhotoList
import dog.snow.androidrecruittest.testutils.createRawUserList
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Test

class GetItemsUseCaseTest {

    @Test
    fun `given repository returns error when getPhotos called then photosSubject emits that error`() {
        val throwable = Throwable("error")
        val useCase = createUseCase(
                repo = mock {
                    on { getPhotos() } doReturn Observable.error(throwable)
                }
        )
        val obs = useCase.photosSubject().test()
        obs.assertError(throwable)
    }

    @Test
    fun `given repository returns rawPhotoList when getPhotos called then photosSubject emits that list`() {
        val list = createRawPhotoList()
        val useCase = createUseCase(
                repo = mock {
                    on { getPhotos() } doReturn Observable.just(list)
                }
        )
        val obs = useCase.photosSubject().test()
        obs.assertValue(list)
    }

    @Test
    fun `given repository returns error when getPhotos called then albumsSubject emits that error`() {
        val throwable = Throwable("error")
        val useCase = createUseCase(
                repo = mock {
                    on { getPhotos() } doReturn Observable.error(throwable)
                }
        )
        val obs = useCase.albumsSubject().test()
        obs.assertError(throwable)
    }

    @Test
    fun `given repository returns rawPhotoList when getPhotos called and returns error when getAlbums called then albumsSubject emits that error`() {
        val throwable = Throwable("error")
        val photoList = createRawPhotoList(albums = 5)
        val albumList = listOf<Int>(1, 2, 3, 4, 5)
        val useCase = createUseCase(
                repo = mock {
                    on { getPhotos() } doReturn Observable.just(photoList)
                    on { getAlbums(albumList) } doReturn Observable.error(throwable)
                }
        )
        val obs = useCase.albumsSubject().test()
        obs.assertError(throwable)
    }

    @Test
    fun `given repository returns rawPhotoList with certain albumIds when getPhotos called then getAlbums with albumIds as argument is called`() {
        val photoList = createRawPhotoList(albums = 5)
        val albumList = listOf<Int>(1, 2, 3, 4, 5)
        val repoMock: PlaceholderRepository = mock {
            on { getPhotos() } doReturn Observable.just(photoList)
        }
        val useCase = createUseCase(repo = repoMock)

        verify(repoMock, times(1)).getPhotos()
        verify(repoMock, times(1)).getAlbums(albumList)
    }

    @Test
    fun `given repository returns rawPhotoList when getPhotos called and rawAlbumList when getAlbums called then albumsSubject emits that rawAlbumList`() {
        val photoList = createRawPhotoList(albums = 5)
        val idList = listOf<Int>(1, 2, 3, 4, 5)
        val albumList = createRawAlbumList(5)

        val repoMock: PlaceholderRepository = mock {
            on { getPhotos() } doReturn Observable.just(photoList)
            on { getAlbums(idList) } doReturn Observable.just(albumList)
        }
        val useCase = createUseCase(repo = repoMock)

        val obs1 = useCase.albumsSubject().test()
        obs1.assertValue(albumList)
    }

    @Test
    fun `given repository returns error when getPhotos called then usersSubject emits that error`() {
        val throwable = Throwable("error")
        val useCase = createUseCase(
                repo = mock {
                    on { getPhotos() } doReturn Observable.error(throwable)
                }
        )
        val obs = useCase.usersSubject().test()
        obs.assertError(throwable)
    }

    @Test
    fun `given repository returns rawPhotoList when getPhotos called and returns error when getAlbums called then usersSubject emits that error`() {
        val throwable = Throwable("error")
        val photoList = createRawPhotoList(albums = 5)
        val albumList = listOf<Int>(1, 2, 3, 4, 5)
        val useCase = createUseCase(
                repo = mock {
                    on { getPhotos() } doReturn Observable.just(photoList)
                    on { getAlbums(albumList) } doReturn Observable.error(throwable)
                }
        )
        val obs = useCase.usersSubject().test()
        obs.assertError(throwable)
    }

    @Test
    fun `given repository returns rawPhotoList with certain albumIds when getPhotos called and rawPhotoAlbums with certain userIds then getUsers with userIds as argument is called`() {
        val photoList = createRawPhotoList(albums = 5)
        val albumIds = listOf<Int>(1, 2, 3, 4, 5)
        val albumList = createRawAlbumList(5, 3)
        val userIds = listOf<Int>(1, 2, 3)

        val repoMock: PlaceholderRepository = mock {
            on { getPhotos() } doReturn Observable.just(photoList)
            on { getAlbums(albumIds) } doReturn Observable.just(albumList)
        }

        val useCase = createUseCase(repo = repoMock)

        verify(repoMock, times(1)).getPhotos()
        verify(repoMock, times(1)).getAlbums(albumIds)
        verify(repoMock, times(1)).getUsers(userIds)
    }

    @Test
    fun `given repository returns rawPhotoList when getPhotos called and rawAlbumList when getAlbums called and error when getUsers called then usersSubject emits that error`() {
        val throwable = Throwable("error")
        val photoList = createRawPhotoList(albums = 5)
        val idList = listOf<Int>(1, 2, 3, 4, 5)
        val albumList = createRawAlbumList(5, 2)
        val userIds = listOf<Int>(1, 2)

        val repoMock: PlaceholderRepository = mock {
            on { getPhotos() } doReturn Observable.just(photoList)
            on { getAlbums(idList) } doReturn Observable.just(albumList)
            on { getUsers(userIds) } doReturn Observable.error(throwable)
        }
        val useCase = createUseCase(repo = repoMock)

        val obs1 = useCase.usersSubject().test()
        obs1.assertError(throwable)
    }

    @Test
    fun `given repository returns rawPhotoList when getPhotos called and rawAlbumList when getAlbums called and rawUserList when getUsers called then usersSubject emits that list`() {
        val photoList = createRawPhotoList(albums = 5)
        val albumIds = listOf<Int>(1, 2, 3, 4, 5)
        val albumList = createRawAlbumList(5, 2)
        val userIds = listOf<Int>(1, 2)
        val userList = createRawUserList(2)
        val repoMock: PlaceholderRepository = mock {
            on { getPhotos() } doReturn Observable.just(photoList)
            on { getAlbums(albumIds) } doReturn Observable.just(albumList)
            on { getUsers(userIds) } doReturn Observable.just(userList)
        }
        val useCase = createUseCase(repo = repoMock)

        val obs1 = useCase.usersSubject().test()
        obs1.assertValue(userList)
    }

    @Test
    fun `given repository returns rawPhotoList, rawAlbumList and rawUserList when initializing then listItem emits list`() {
        val photoList = createRawPhotoList(100, 5)
        val albumIds = listOf<Int>(1, 2, 3, 4, 5)
        val albumList = createRawAlbumList(5, 2)
        val userIds = listOf<Int>(1, 2)
        val userList = createRawUserList(2)
        val itemList = createListItems(100, 5, 2)
        val repoMock: PlaceholderRepository = mock {
            on { getPhotos() } doReturn Observable.just(photoList)
            on { getAlbums(albumIds) } doReturn Observable.just(albumList)
            on { getUsers(userIds) } doReturn Observable.just(userList)
        }
        val useCase = createUseCase(repo = repoMock)

        val obs1 = useCase.listItems.test()
        obs1.assertValue(itemList)
    }

    @Test
    fun `given repository returns rawPhotoList and error when initializing then listItem emits that error`() {
        val throwable = Throwable("error")
        val photoList = createRawPhotoList(100, 5)
        val albumIds = listOf<Int>(1, 2, 3, 4, 5)
        val repoMock: PlaceholderRepository = mock {
            on { getPhotos() } doReturn Observable.just(photoList)
            on { getAlbums(albumIds) } doReturn Observable.error(throwable)
        }
        val useCase = createUseCase(repo = repoMock)

        val obs1 = useCase.listItems.test()
        obs1.assertError(throwable)
    }


    private fun createUseCase(
            repo: PlaceholderRepository = mock()): GetItemsUseCase {
        return GetItemsUseCase(repo, schedulers = mock {
            on { io() } doReturn Schedulers.trampoline()
            on { mainThread() } doReturn Schedulers.trampoline()
        })
    }
}