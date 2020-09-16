package dog.snow.androidrecruittest.usecases

import dog.snow.androidrecruittest.repository.PlaceholderRepository
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.BehaviorSubject

class FetchDataUseCaseImpl(private val repository: PlaceholderRepository) : FetchDataUseCase {
    private var sSubject = BehaviorSubject.create<Int>()

    init {
        subscribeToRepositoryRawLists()
    }

    private fun subscribeToRepositoryRawLists() {
        repository.getRawPhotos()
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
        repository.refetchPhotos()
        sSubject = BehaviorSubject.create()
        subscribeToRepositoryRawLists()
    }
}