package dog.snow.androidrecruittest.di

import dog.snow.androidrecruittest.network.HttpClientFactory
import dog.snow.androidrecruittest.network.JsonPlaceholderApi
import dog.snow.androidrecruittest.repository.PlaceholderRepository
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppGraph {
    private val okHttpClient = HttpClientFactory().create()
    private val jsonPlaceholderApi = Retrofit.Builder()
            .baseUrl(HttpClientFactory.JSON_PLACEHOLDER_API_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build().create(JsonPlaceholderApi::class.java)

    val placeholderRepository = PlaceholderRepository(jsonPlaceholderApi)
}