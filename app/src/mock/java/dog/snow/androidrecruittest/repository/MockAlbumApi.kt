package dog.snow.androidrecruittest.repository

import dog.snow.androidrecruittest.repository.loaders.MockModelLoader
import dog.snow.androidrecruittest.repository.model.RawAlbum
import dog.snow.androidrecruittest.repository.service.AlbumService

class MockAlbumApi(
    private val mockModelLoader: MockModelLoader
) : AlbumService {
    fun loadAlbums(): List<RawAlbum> {
        val albums = mockModelLoader.loadAlbums(RawAlbum::class.java)
        TODO("not implemented")
    }

    fun loadAlbum(albumId: Int): RawAlbum {
        val album = mockModelLoader.loadAlbums(RawAlbum::class.java)?.find { it.id == albumId }
        TODO("not implemented")
    }
}