package dog.snow.androidrecruittest.usecases

import io.reactivex.rxjava3.core.Single

interface FetchDataUseCase {
    fun fetchingDataEnded(): Single<Int>
    fun refetch()
}