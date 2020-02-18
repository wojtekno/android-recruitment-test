package dog.snow.androidrecruittest.repository

import dog.snow.androidrecruittest.repository.loaders.MockModelLoader
import dog.snow.androidrecruittest.repository.model.RawUser
import dog.snow.androidrecruittest.repository.service.UserService

class MockUserApi(
    private val mockModelLoader: MockModelLoader
) : UserService {
    fun loadUsers(): List<RawUser> {
        val users = mockModelLoader.loadUsers(RawUser::class.java)
        TODO("not implemented")
    }

    fun loadUser(photoId: Int): RawUser {
        val user = mockModelLoader.loadUsers(RawUser::class.java)?.find { it.id == photoId }
        TODO("not implemented")
    }
}