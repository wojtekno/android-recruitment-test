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
import dog.snow.androidrecruittest.ui.detail.DetailsVmFactory
import dog.snow.androidrecruittest.ui.list.ListVmFactory
import dog.snow.androidrecruittest.ui.splash.SplashVmFactory
import dog.snow.androidrecruittest.usecases.*
import timber.log.Timber.d

class AppGraph(context: Context) {
    init {
        d("initializing AppGraph")
    }

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
    val fetchDataUseCase: FetchDataUseCase = FetchDataUseCaseImpl(placeholderRepository)
    val getDetailUseCase: GetDetailUseCase = GetDetailUseCaseImpl(placeholderRepository, resourceProvider)

    val splashVmFactory: SplashVmFactory = SplashVmFactory(fetchDataUseCase, schedulerProvider, resourceProvider)
    val listVMFactory: ListVmFactory = ListVmFactory(getItemsUseCase, schedulerProvider, resourceProvider)
    val detailsVmFactory: DetailsVmFactory = DetailsVmFactory(getDetailUseCase, schedulerProvider)

}