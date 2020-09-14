package dog.snow.androidrecruittest.usecases

import io.reactivex.rxjava3.core.Single

interface NotifyDataFetchedUseCase {
    fun fetchingDataEnded(): Single<Int>
}