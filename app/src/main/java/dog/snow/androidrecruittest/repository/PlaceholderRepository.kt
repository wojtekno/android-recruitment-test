package dog.snow.androidrecruittest.repository

import dog.snow.androidrecruittest.repository.model.RawAlbum
import dog.snow.androidrecruittest.repository.model.RawPhoto
import dog.snow.androidrecruittest.repository.model.RawUser
import io.reactivex.rxjava3.core.Observable

interface PlaceholderRepository {
    fun getRawPhotos(): Observable<List<RawPhoto>>
    fun getRawAlbums(): Observable<List<RawAlbum>>
    fun getRawUsers(): Observable<List<RawUser>>
    fun refetchPhotos(quantity: Int = 100)
}