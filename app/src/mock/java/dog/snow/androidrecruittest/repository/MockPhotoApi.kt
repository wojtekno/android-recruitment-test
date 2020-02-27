package dog.snow.androidrecruittest.repository

import dog.snow.androidrecruittest.repository.loaders.MockModelLoader
import dog.snow.androidrecruittest.repository.model.RawPhoto
import dog.snow.androidrecruittest.repository.service.PhotoService

class MockPhotoApi(
    private val mockModelLoader: MockModelLoader
) : PhotoService {
    fun loadPhotos(): List<RawPhoto> {
        val photos = mockModelLoader.loadPhotos(RawPhoto::class.java)
        TODO("not implemented")
    }

    fun loadPhoto(photoId: Int): RawPhoto {
        val photo = mockModelLoader.loadPhotos(RawPhoto::class.java)?.find { it.id == photoId }
        TODO("not implemented")
    }
}