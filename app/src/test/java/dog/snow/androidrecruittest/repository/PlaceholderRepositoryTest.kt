package dog.snow.androidrecruittest.repository

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import dog.snow.androidrecruittest.network.JsonPlaceholderApi
import dog.snow.androidrecruittest.repository.model.RawPhoto
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.observers.TestObserver
import org.junit.Test

class PlaceholderRepositoryTest {

    @Test
    fun `given jsonPlaceholderApi returns error when initializing PlaceHolderRepository then photosObs returns that Error`() {
        val throwable = Throwable("error")
        val repo = createMainRepo(placeholderApi = mock {
            on { getPhotos() } doReturn Observable.error(throwable)
        })

        val testObserver = repo.photosObs.test()
        testObserver.assertError(throwable)
        testObserver.dispose()
    }

    @Test
    fun `given jsonPlaceholderApi returns empty list when initializing PlaceHolderRepository then photosObs returns empty list `() {
        val repo = createMainRepo(placeholderApi = mock {
            on { getPhotos() } doReturn Observable.just(emptyList())
        })

        val testObserver: TestObserver<List<RawPhoto>> = repo.photosObs.test()
        testObserver.assertValue(emptyList())
        testObserver.dispose()
    }

    @Test
    fun `given jsonPlaceholderApi returns list when initializing PlaceHolderRepository then photosObs returns that list`() {
        val list = createPhotoList()
        val repo = createMainRepo(placeholderApi = mock {
            on { getPhotos() } doReturn Observable.just(list)
        })

        val testObserver = repo.photosObs.test()
        testObserver.assertValue(list)
        testObserver.dispose()
    }


    private fun createMainRepo(
            placeholderApi: JsonPlaceholderApi = mock {
                on { getPhotos() } doReturn Observable.never()
            })
            : PlaceholderRepository {
        return PlaceholderRepository(placeholderApi)
    }

    private fun createPhotoList(photos: Int = 100, albums: Int = 10): List<RawPhoto> {
        val list = mutableListOf<RawPhoto>()
        for (i in 0..photos) {
            val photo = RawPhoto(i, i % albums, "Photo title $i", "urlOfPhoto$i", "thumbNailOfPhoto$i")
            list.add(photo)
        }
        return list
    }
}