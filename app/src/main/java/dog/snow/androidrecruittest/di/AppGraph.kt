package dog.snow.androidrecruittest.di

import android.content.Context
import dog.snow.androidrecruittest.network.HttpClientFactory
import dog.snow.androidrecruittest.network.PlaceholderApi
import dog.snow.androidrecruittest.network.RetrofitClientFactory
import dog.snow.androidrecruittest.repository.PlaceholderRepository
import dog.snow.androidrecruittest.repository.PlaceholderRepositoryImpl
import dog.snow.androidrecruittest.resourceprovider.AndroidResourceProvider
import dog.snow.androidrecruittest.resourceprovider.ResourceProvider
import dog.snow.androidrecruittest.scheduler.SchedulerProvider
import dog.snow.androidrecruittest.scheduler.SchedulerProviderImpl
import dog.snow.androidrecruittest.usecases.GetListItemsUseCase
import dog.snow.androidrecruittest.usecases.GetListItemsUseCaseImpl
import dog.snow.androidrecruittest.usecases.NotifyDataFetchedUseCase
import dog.snow.androidrecruittest.usecases.NotifyDataFetchedUseCaseImpl

class AppGraph(context: Context) {
    //network
    private val okHttpClient = HttpClientFactory().create()
    private val jsonPlaceholderApi = RetrofitClientFactory()
            .create(okHttpClient)
            .create(PlaceholderApi::class.java)

    //providers
    private val schedulerProvider: SchedulerProvider = SchedulerProviderImpl()
    private val resourceProvider: ResourceProvider = AndroidResourceProvider(context)

    //repository
    private val placeholderRepository: PlaceholderRepository = PlaceholderRepositoryImpl(jsonPlaceholderApi, schedulerProvider)

    //useCases
    val getItemsUseCase: GetListItemsUseCase = GetListItemsUseCaseImpl(placeholderRepository, schedulerProvider, resourceProvider)
    val notifyDataFetchedUseCase: NotifyDataFetchedUseCase = NotifyDataFetchedUseCaseImpl(placeholderRepository)

}