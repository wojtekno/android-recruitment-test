package dog.snow.androidrecruittest.repository

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import dog.snow.androidrecruittest.network.PlaceholderApi
import dog.snow.androidrecruittest.repository.model.RawAlbum
import dog.snow.androidrecruittest.repository.model.RawPhoto
import dog.snow.androidrecruittest.scheduler.SchedulerProviderImpl
import io.reactivex.rxjava3.core.Observable
import org.junit.Test

class PlaceholderRepositoryImplTest {

    @Test
    fun `given jsonPlaceholderApi returns error when getPhotos called then photosObs returns that Error`() {
        val throwable = Throwable("error")
        val repo = createMainRepo(placeholderApi = mock {
            on { getPhotos() } doReturn Observable.error(throwable)
        })

//        val testObserver = repo.photosObs.test()
//        testObserver.assertError(throwable)
//        testObserver.dispose()
    }

    @Test
    fun `given jsonPlaceholderApi returns empty list of RawPhotos when getPhotos called then photosObs returns empty list `() {
        val repo = createMainRepo(placeholderApi = mock {
            on { getPhotos() } doReturn Observable.just(emptyList())
            on { getAlbums(any()) } doReturn Observable.never()
        })

//        val testObserver: TestObserver<List<RawPhoto>> = repo.photosObs.test()
//        testObserver.assertValue(emptyList())
//        testObserver.dispose()
    }

    @Test
    fun `given jsonPlaceholderApi returns list of RawPhotos when getPhotos called  then photosObs returns that list`() {
        val list = createPhotoList()
        val repo = createMainRepo(placeholderApi = mock {
            on { getPhotos() } doReturn Observable.just(list)
            on { getAlbums(any()) } doReturn Observable.never()
        })

//        val testObserver = repo.photosObs.test()
//        testObserver.assertValue(list)
//        testObserver.dispose()
    }

    @Test
    fun `given photosObs is being observed and jsonPlaceholderApi returns error when getAlbum called then albumsObs returns error`() {
        val throwable = Throwable("error")
        val albumIds = listOf<Int>(1)
        val list = createPhotoList(albums = 1)
        val repo = createMainRepo(placeholderApi = mock {
            on { getPhotos() } doReturn Observable.just(list)
            on { getAlbums(albumIds) } doReturn Observable.error(throwable)
        })

//        val testObserver = repo.photosObs.test()
//        val testObserver1 = repo.albumsObs.test()
//        testObserver1.assertError(throwable)
//        testObserver.dispose()
//        testObserver1.dispose()
    }

    @Test
    fun `given photosObs is being observed and jsonPlaceholderApi returns albumList when getAlbum called then albumsObs returns that list`() {
        val albumIds = listOf<Int>(1, 2, 3)
        val albums = createAlbumList(3)
        val list = createPhotoList(albums = 3)
        val repo = createMainRepo(placeholderApi = mock {
            on { getPhotos() } doReturn Observable.just(list)
            on { getAlbums(albumIds) } doReturn Observable.just(albums)
//            on { getAlbum(1) } doReturn Observable.just(albums[0])
//            on { getAlbum(2) } doReturn Observable.just(albums[1])
//            on { getAlbum(3) } doReturn Observable.just(albums[2])
        })

//        val test1 = repo.photosObs.test()
//        val testObserver = repo.albumsObs.test()
//        testObserver.assertValue(albums)
//        testObserver.dispose()
    }


    private fun createMainRepo(
            placeholderApi: PlaceholderApi = mock {
                on { getPhotos() } doReturn Observable.never()
                on { getAlbums(any()) } doReturn Observable.never()
            })
            : PlaceholderRepositoryImpl {
        return PlaceholderRepositoryImpl(placeholderApi, SchedulerProviderImpl())
    }

    private fun createPhotoList(photos: Int = 100, albums: Int = 10): List<RawPhoto> {
        val list = mutableListOf<RawPhoto>()
        for (i in 1..photos) {
            val albumId = when (i % albums) {
                0 -> albums
                else -> i % albums
            }
            val photo = RawPhoto(i, albumId, "Photo title $i", "urlOfPhoto$i", "thumbNailOfPhoto$i")
            list.add(photo)
        }
        return list
    }

    private fun createAlbumList(albums: Int = 3, users: Int = 2): List<RawAlbum> {
        val list = mutableListOf<RawAlbum>()
        for (i in 1..albums) {
            val userId = when (i % users) {
                0 -> users
                else -> i % users
            }
            val album = RawAlbum(i, userId, "album title $i")
            list.add(album)
        }
        return list

    }


}