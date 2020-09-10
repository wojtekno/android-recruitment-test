package dog.snow.androidrecruittest.di

import dog.snow.androidrecruittest.network.HttpClientFactory
import dog.snow.androidrecruittest.network.PlaceholderApi
import dog.snow.androidrecruittest.network.RetrofitClientFactory
import dog.snow.androidrecruittest.repository.PlaceholderRepository
import dog.snow.androidrecruittest.repository.PlaceholderRepositoryImpl
import dog.snow.androidrecruittest.scheduler.SchedulerProvider
import dog.snow.androidrecruittest.scheduler.SchedulerProviderImpl
import dog.snow.androidrecruittest.usecases.GetItemsUseCase

class AppGraph {
    private val okHttpClient = HttpClientFactory().create()
    private val jsonPlaceholderApi = RetrofitClientFactory()
            .create(okHttpClient)
            .create(PlaceholderApi::class.java)
    private val schedulerProvider: SchedulerProvider = SchedulerProviderImpl()

    private val placeholderRepository: PlaceholderRepository = PlaceholderRepositoryImpl(jsonPlaceholderApi, schedulerProvider)
    val getItemsUseCase = GetItemsUseCase(placeholderRepository, schedulerProvider)
}