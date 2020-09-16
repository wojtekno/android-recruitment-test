package dog.snow.androidrecruittest.ui

import androidx.lifecycle.ViewModel
import dog.snow.androidrecruittest.ui.model.ListItem

class MainViewModel : ViewModel() {
    private lateinit var clickedItem: Pair<Int, ListItem>
    var transName: String = ""

    fun setClickedItem(listId: Int, listItem: ListItem) {
        clickedItem = Pair(listId, listItem)
    }

    fun getClickedItem(): Pair<Int, ListItem> = clickedItem
}