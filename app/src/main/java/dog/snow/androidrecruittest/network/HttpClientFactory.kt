package dog.snow.androidrecruittest.network

import dog.snow.androidrecruittest.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import okhttp3.logging.HttpLoggingInterceptor

class HttpClientFactory {

    companion object{
        const val JSON_PLACEHOLDER_API_URL = "https://jsonplaceholder.typicode.com"
    }

    fun create(): OkHttpClient {
        val httpBuilder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            httpBuilder.addInterceptor(loggingInterceptor)
        }
        return httpBuilder.build()
    }
}