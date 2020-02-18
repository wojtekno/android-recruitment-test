package dog.snow.androidrecruittest.repository

import dog.snow.androidrecruittest.repository.loaders.MockModelLoader
import dog.snow.androidrecruittest.repository.model.RawUser
import dog.snow.androidrecruittest.repository.service.UserService

class MockUserApi(
    private val mockModelLoader: MockModelLoader
) : UserService {
//    override suspend fun loadUsers(): Response<List<RawUser>> {
//        val users = mockModelLoader.loadUsers(RawUser::class.java)
//    }

//    override suspend fun loadUser(photoId: Int): Response<RawUser> {
//        val user = mockModelLoader.loadUsers(RawUser::class.java)?.find { it.id == photoId }
//    }
}