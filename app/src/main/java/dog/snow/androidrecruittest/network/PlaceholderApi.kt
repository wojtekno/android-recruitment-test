package dog.snow.androidrecruittest.network

import dog.snow.androidrecruittest.repository.model.RawAlbum
import dog.snow.androidrecruittest.repository.model.RawPhoto
import dog.snow.androidrecruittest.repository.model.RawUser
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PlaceholderApi {
    @GET("photos")
    fun getPhotos(@Query("_limit") quantity: Int = 100): Observable<List<RawPhoto>>

    @GET("albums/{id}")
    fun getAlbum(@Path("id") id: Int): Observable<RawAlbum>

    @GET("albums")
    fun getAlbums(@Query("id") ids: List<Int>): Observable<List<RawAlbum>>

    @GET("users")
    fun getUsers(@Query("id") userIds: List<Int>): Observable<List<RawUser>>

}