package dog.snow.androidrecruittest.repository

import dog.snow.androidrecruittest.repository.loaders.MockModelLoader
import dog.snow.androidrecruittest.repository.model.RawPhoto
import dog.snow.androidrecruittest.repository.service.PhotoService

class MockPhotoApi(
    private val mockModelLoader: MockModelLoader
) : PhotoService {
//    override suspend fun loadPhotos(): Response<List<RawPhoto>> {
//        val photos = mockModelLoader.loadPhotos(RawPhoto::class.java)
//    }

//    override suspend fun loadPhoto(photoId: Int): Response<RawPhoto> {
//        val photo = mockModelLoader.loadPhotos(RawPhoto::class.java)?.find { it.id == photoId }
//    }
}