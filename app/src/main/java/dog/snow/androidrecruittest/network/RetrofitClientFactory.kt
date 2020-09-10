package dog.snow.androidrecruittest.network

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClientFactory {
    fun create(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(HttpClientFactory.JSON_PLACEHOLDER_API_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
    }
}