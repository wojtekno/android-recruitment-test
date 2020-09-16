package dog.snow.androidrecruittest.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import dog.snow.androidrecruittest.R
import dog.snow.androidrecruittest.scheduler.SchedulerProvider
import dog.snow.androidrecruittest.ui.splash.SplashViewModel
import dog.snow.androidrecruittest.usecases.FetchDataUseCase
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Rule
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class SplashViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun `given useCase hasn't anything yet when initializing then downloadingError() returns emptyLiveData`() {
        val vm = createVM(useCase = mock {
            on { fetchingDataEnded() } doReturn Single.never()
        }
        )

        expectThat(vm.downloadingError().value).isEqualTo(null)
    }

    @Test
    fun `given useCase returns error when initializing then downloadingError() returns ErrorMessage `() {
        val throwableMock = Throwable("error")
        val vm = createVM(useCase = mock {
            on { fetchingDataEnded() } doReturn Single.error(throwableMock)
        }
        )

        expectThat(vm.downloadingError().value).isEqualTo(throwableMock.message)
    }

    @Test
    fun `given useCase returns value when initializing then downloadingError() returns empty LiveData `() {
        val throwableMock = Throwable("error")
        val vm = createVM(useCase = mock {
            on { fetchingDataEnded() } doReturn Single.just(1)
        }
        )

        expectThat(vm.downloadingError().value).isEqualTo(null)
    }


    @Test
    fun `given useCase hasn't anything yet when initializing then isDataDownloaded() returns emptyLiveData`() {
        val vm = createVM(useCase = mock {
            on { fetchingDataEnded() } doReturn Single.never()
        }
        )

        expectThat(vm.downloadingCompletion().value).isEqualTo(null)
    }

    @Test
    fun `given useCase returns error when initializing when initializing then isDataDownloaded() returns emptyLiveData`() {
        val throwableMock = Throwable("error")
        val vm = createVM(useCase = mock {
            on { fetchingDataEnded() } doReturn Single.error(throwableMock)
        }
        )

        expectThat(vm.downloadingCompletion().value).isEqualTo(null)
    }

    @Test
    fun `given useCase returns 1 when initializing when initializing then isDataDownloaded() returns data_downloaded_partially_message`() {
        val throwableMock = Throwable("error")
        val vm = createVM(useCase = mock {
            on { fetchingDataEnded() } doReturn Single.just(1)
        }
        )

        expectThat(vm.downloadingCompletion().value).isEqualTo("Could't download complete data. Some info may be missing")
    }

    @Test
    fun `given useCase returns 2 when initializing when initializing then isDataDownloaded() returns data_downloaded_partially_message`() {
        val throwableMock = Throwable("error")
        val vm = createVM(useCase = mock {
            on { fetchingDataEnded() } doReturn Single.just(2)
        }
        )

        expectThat(vm.downloadingCompletion().value).isEqualTo("Could't download complete data. Some info may be missing")
    }

    @Test
    fun `given useCase returns 3 when initializing when initializing then isDataDownloaded() returns downloading_successful`() {
        val throwableMock = Throwable("error")
        val vm = createVM(useCase = mock {
            on { fetchingDataEnded() } doReturn Single.just(3)
        }
        )

        expectThat(vm.downloadingCompletion().value).isEqualTo("success")
    }

    private fun createVM(useCase: FetchDataUseCase = mock(),
                         schedulers: SchedulerProvider = mock {
                             on { io() } doReturn Schedulers.trampoline()
                             on { mainThread() } doReturn Schedulers.trampoline()
                         }): SplashViewModel {

        return SplashViewModel(useCase, schedulers, resourceProvider = mock {
            on { getString(R.string.data_downloaded_partially_message) } doReturn "Could\'t download complete data. Some info may be missing"
            on { getString(R.string.downloading_successful) } doReturn "success"
        })
    }
}


