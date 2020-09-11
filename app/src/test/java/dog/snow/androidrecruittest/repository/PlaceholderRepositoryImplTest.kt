package dog.snow.androidrecruittest.repository

import com.nhaarman.mockitokotlin2.*
import dog.snow.androidrecruittest.network.PlaceholderApi
import dog.snow.androidrecruittest.scheduler.SchedulerProvider
import dog.snow.androidrecruittest.testutils.createRawAlbumList
import dog.snow.androidrecruittest.testutils.createRawPhotoList
import dog.snow.androidrecruittest.testutils.createRawUserList
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Test

class PlaceholderRepositoryImplTest {

    @Test
    fun `given jsonPlaceholderApi returns error when getPhotos called then getRawPhotos() emits that Error`() {
        val throwable = Throwable("error")
        val repo = createMainRepo(placeholderApi = mock {
            on { getPhotos() } doReturn Observable.error(throwable)
        })

        repo.getRawPhotos().test()
                .assertError(throwable)
                .dispose()
    }

    @Test
    fun `given jsonPlaceholderApi returns rawPhotoList when getPhotos() called then getRawPhotos() emits that list`() {
        val list = createRawPhotoList(100)
        val repo = createMainRepo(placeholderApi = mock {
            on { getPhotos() } doReturn Observable.just(list)
        })

        repo.getRawPhotos().test()
                .assertValue(list)
                .dispose()
    }

    @Test
    fun `given jsonPlaceholderApi returns error when getPhotos() called then then getAlbums() not called`() {
        val throwable = Throwable("error")
        val apiMock: PlaceholderApi = mock {
            on { getPhotos() } doReturn Observable.error(throwable)
        }
        val repo = createMainRepo(placeholderApi = apiMock)
        verify(apiMock, times(0)).getAlbums(any())
    }

    @Test
    fun `given jsonPlaceholderApi returns error when getPhotos() called then getRawAlbums() is empty`() {
        val throwable = Throwable("error")
        val repo = createMainRepo(placeholderApi = mock {
            on { getPhotos() } doReturn Observable.error(throwable)
        })

        repo.getRawAlbums().test()
                .assertEmpty()
                .dispose()
    }

    @Test
    fun `given returned rawPhotoList has albumIdList when getPhotos() called then getAlbums(albumIdList) called`() {
        val list = createRawPhotoList(100, 15)
        val albumIds = (1..15).toList()
        val placeholderMock: PlaceholderApi = mock {
            on { getPhotos() } doReturn Observable.just(list)
        }
        val repo = createMainRepo(placeholderApi = placeholderMock)

        verify(placeholderMock).getAlbums(albumIds)
    }

    @Test
    fun `given jsonPlaceholderApi returns rawAlbumList when getAlbums() called then getRawAlbums emits that list`() {
        val photoList = createRawPhotoList(100, 15)
        val albumIds = (1..15).toList()
        val albumList = createRawAlbumList(15)
        val repo = createMainRepo(placeholderApi = mock {
            on { getPhotos() } doReturn Observable.just(photoList)
            on { getAlbums(albumIds) } doReturn Observable.just(albumList)
        })

        repo.getRawAlbums().test()
                .assertValue(albumList)
                .dispose()
    }

    @Test
    fun `given jsonPlaceholderApi returns error when getPhotos() called then getUsers() is not called `() {
        val throwable = Throwable("error")
        val placeholderMock: PlaceholderApi = mock {
            on { getPhotos() } doReturn Observable.error(throwable)
        }
        val repo = createMainRepo(placeholderApi = placeholderMock)
        verify(placeholderMock, times(0)).getUsers(any())
    }

    @Test
    fun `given jsonPlaceholderApi returns error when getAlbums() called then getUsers() is not called `() {
        val throwable = Throwable("error")
        val photoList = createRawPhotoList(100, 15)
        val placeholderMock: PlaceholderApi = mock {
            on { getPhotos() } doReturn Observable.just(photoList)
            on { getAlbums(any()) } doReturn Observable.error(throwable)
        }
        val repo = createMainRepo(placeholderApi = placeholderMock)
        verify(placeholderMock, times(0)).getUsers(any())
    }

    @Test
    fun `given jsonPlaceholderApi returns error when getAlbums() called then getRawUsers() is empty`() {
        val throwable = Throwable("error")
        val photoList = createRawPhotoList(100, 15)
        val albumIds = (1..15).toList()
        val placeholderMock: PlaceholderApi = mock {
            on { getPhotos() } doReturn Observable.just(photoList)
            on { getAlbums(albumIds) } doReturn Observable.error(throwable)
        }
        val repo = createMainRepo(placeholderApi = placeholderMock)
        repo.getRawUsers().test()
                .assertEmpty()
                .dispose()

    }

    @Test
    fun `given returned rawAlbumList has userIdList when getAlbums() called then getUsers(userIdList) is called `() {
        val photoList = createRawPhotoList(100, 15)
        val albumIds = (1..15).toList()
        val albumList = createRawAlbumList(15, 4)
        val userIds = (1..4).toList()
        val placeholderMock: PlaceholderApi = mock {
            on { getPhotos() } doReturn Observable.just(photoList)
            on { getAlbums(albumIds) } doReturn Observable.just(albumList)
        }
        val repo = createMainRepo(placeholderApi = placeholderMock)
        verify(placeholderMock, times(1)).getUsers(userIds)
    }

    @Test
    fun `given jsonPlaceholderApi returns rawUserList when getUsers() called then getRawUsers() emits that list`() {
        val photoList = createRawPhotoList(100, 15)
        val albumIds = (1..15).toList()
        val albumList = createRawAlbumList(15, 4)
        val userIds = (1..4).toList()
        val userList = createRawUserList(4)
        val placeholderMock: PlaceholderApi = mock {
            on { getPhotos() } doReturn Observable.just(photoList)
            on { getAlbums(albumIds) } doReturn Observable.just(albumList)
            on { getUsers(userIds) } doReturn Observable.just(userList)
        }
        val repo = createMainRepo(placeholderApi = placeholderMock)
        repo.getRawUsers().test()
                .assertValue(userList)
                .dispose()
    }

    @Test
    fun `given jsonPlaceholderApi returns error when getUsers() called then getRawUsers() emits that error`() {
        val throwable = Throwable("error")
        val photoList = createRawPhotoList(100, 15)
        val albumIds = (1..15).toList()
        val albumList = createRawAlbumList(15, 4)
        val userIds = (1..4).toList()
        val placeholderMock: PlaceholderApi = mock {
            on { getPhotos() } doReturn Observable.just(photoList)
            on { getAlbums(albumIds) } doReturn Observable.just(albumList)
            on { getUsers(userIds) } doReturn Observable.error(throwable)
        }
        val repo = createMainRepo(placeholderApi = placeholderMock)
        repo.getRawUsers().test()
                .assertError(throwable)
                .dispose()
    }

    @Test
    fun `given jsonPlaceholderApi returns rawPhotoList, rawAlbumList, rawUserList when reFetch called then Observables emits updated lists`() {
        //set1
        val photoList = createRawPhotoList(100, 15)
        val albumIds = (1..15).toList()
        val albumList = createRawAlbumList(15, 4)
        val userIds = (1..4).toList()
        val userList = createRawUserList(4)
        //set2
        val photoList2 = createRawPhotoList(200, 35)
        val albumIds2 = (1..35).toList()
        val albumList2 = createRawAlbumList(35, 25)
        val userIds2 = (1..25).toList()
        val userList2 = createRawUserList(25)

        var placeholderMock: PlaceholderApi = mock {
            on { getPhotos() } doReturn Observable.just(photoList)
            on { getPhotos(200) } doReturn Observable.just(photoList2)
            on { getAlbums(albumIds) } doReturn Observable.just(albumList)
            on { getAlbums(albumIds2) } doReturn Observable.just(albumList2)
            on { getUsers(userIds) } doReturn Observable.just(userList)
            on { getUsers(userIds2) } doReturn Observable.just(userList2)
        }
        val repo = createMainRepo(placeholderApi = placeholderMock)
        val rawUsers = repo.getRawUsers().test()
                .assertValue(userList)
        val rawAlbums = repo.getRawAlbums().test()
                .assertValue(albumList)
        val rawPhotos = repo.getRawPhotos().test()
                .assertValue(photoList)

        repo.refetchPhotos(200)

        rawPhotos.assertValueAt(1, photoList2)
        rawUsers.assertValueAt(1, userList2)
        rawAlbums.assertValueAt(1, albumList2)
    }


    private fun createMainRepo(
            placeholderApi: PlaceholderApi = mock {
                on { getPhotos() } doReturn Observable.never()
                on { getAlbums(any()) } doReturn Observable.never()
                on { getUsers(any()) } doReturn Observable.never()
            },
            schedulerProvider: SchedulerProvider = mock {
                on { io() } doReturn Schedulers.trampoline()
                on { mainThread() } doReturn Schedulers.trampoline()
            })
            : PlaceholderRepository {
        return PlaceholderRepositoryImpl(placeholderApi, schedulerProvider)
    }
}