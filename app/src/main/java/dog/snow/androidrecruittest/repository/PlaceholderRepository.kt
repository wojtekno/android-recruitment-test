package dog.snow.androidrecruittest.repository

import dog.snow.androidrecruittest.repository.model.RawAlbum
import dog.snow.androidrecruittest.repository.model.RawPhoto
import dog.snow.androidrecruittest.repository.model.RawUser
import io.reactivex.rxjava3.core.Observable

interface PlaceholderRepository {
    fun getPhotos(quantity: Int = 100): Observable<List<RawPhoto>>
    fun getAlbums(albumIds: List<Int>): Observable<List<RawAlbum>>
    fun getUsers(userIds: List<Int>): Observable<List<RawUser>>
}