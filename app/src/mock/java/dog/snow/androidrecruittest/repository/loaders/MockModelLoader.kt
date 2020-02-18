package dog.snow.androidrecruittest.repository.loaders

class MockModelLoader {
    private val mockLoader: MockLoader = MockLoader.createResourceLoader()

    fun <T> loadPhotos(classObject: Class<T>): List<T>? {
        val json = mockLoader.loadJson("mocks/photos.json")
        TODO("not implemented")
    }

    fun <T> loadAlbums(classObject: Class<T>): List<T>? {
        val json = mockLoader.loadJson("mocks/albums.json")
        TODO("not implemented")
    }

    fun <T> loadUsers(classObject: Class<T>): List<T>? {
        val json = mockLoader.loadJson("mocks/users.json")
        TODO("not implemented")
    }
}