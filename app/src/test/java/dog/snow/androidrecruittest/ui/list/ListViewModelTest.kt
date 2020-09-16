package dog.snow.androidrecruittest.ui.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import dog.snow.androidrecruittest.testutils.createListItems
import dog.snow.androidrecruittest.usecases.GetListItemsUseCase
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.Rule
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class ListViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Test
    fun `provide listUseCase hasn't emitted value yet when initializing then listItemsLd() value is null `() {
        val vm = createVm()
        expectThat(vm.listItemsLd().value) isEqualTo (null)
    }

    @Test
    fun `provide listUseCase emitted listItemsList when initializing then listItemsLd() value is that list `() {
        val listItems = createListItems()
        val vm = createVm(listItemsUseCase = mock {
            on { getListItems() } doReturn Observable.just(listItems)
        })
        expectThat(vm.listItemsLd().value) isEqualTo (listItems)
    }


    private fun createVm(listItemsUseCase: GetListItemsUseCase = mock {
        on { getListItems() } doReturn Observable.never()
    }): ListViewModel {

        return ListViewModel(listItemsUseCase,
                schedulers = mock {
                    on { io() } doReturn Schedulers.trampoline()
                    on { mainThread() } doReturn Schedulers.trampoline()
                })
    }
}