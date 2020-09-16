package dog.snow.androidrecruittest.usecases

import dog.snow.androidrecruittest.repository.PlaceholderRepository
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.BehaviorSubject
import timber.log.Timber
import java.util.concurrent.TimeUnit

class FetchDataUseCaseImpl(private val repository: PlaceholderRepository) : FetchDataUseCase {
    private val sSubject = BehaviorSubject.create<Int>()

    init {
        repository.getRawPhotos().delay(1000, TimeUnit.MILLISECONDS)
                .subscribeBy(
                        onError = { sSubject.onError(it) },
                        onNext = {}
                )

        repository.getRawAlbums()
                .subscribeBy(
                        onError = { sSubject.onNext(1) },
                        onNext = {}
                )

        repository.getRawUsers()
                .subscribeBy(
                        onError = { sSubject.onNext(2) },
                        onNext = { sSubject.onNext(3) }
                )

    }

    override fun fetchingDataEnded(): Single<Int> = sSubject.firstOrError()

    override fun refetch() {
        Timber.d("refetching")
        repository.refetchPhotos(200)
    }
}