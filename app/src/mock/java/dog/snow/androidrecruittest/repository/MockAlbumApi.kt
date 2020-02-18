package dog.snow.androidrecruittest.repository

import dog.snow.androidrecruittest.repository.loaders.MockModelLoader
import dog.snow.androidrecruittest.repository.model.RawAlbum
import dog.snow.androidrecruittest.repository.service.AlbumService

class MockAlbumApi(
    private val mockModelLoader: MockModelLoader
) : AlbumService {
//    override suspend fun loadAlbums(): Response<List<RawAlbum>> {
//        val albums = mockModelLoader.loadAlbums(RawAlbum::class.java)
//    }

//    override suspend fun loadAlbum(albumId: Int): Response<RawAlbum> {
//        val album = mockModelLoader.loadAlbums(RawAlbum::class.java)?.find { it.id == albumId }
//    }
}