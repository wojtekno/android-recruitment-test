package dog.snow.androidrecruittest.network

import dog.snow.androidrecruittest.repository.model.RawPhoto
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface JsonPlaceholderApi {
    @GET("photos")
    fun getPhotos(@Query("_limit") quantity: Int = 100): Observable<List<RawPhoto>>


}