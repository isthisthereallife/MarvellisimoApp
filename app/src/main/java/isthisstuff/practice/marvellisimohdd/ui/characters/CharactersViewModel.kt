package isthisstuff.practice.marvellisimohdd.ui.characters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CharactersViewModel : ViewModel() {

    var itemsList = MutableLiveData<List<String>>()

    init {
        itemsList.value = listOf("Test 1", "Test 2")
    }

    fun addItem() {
        itemsList.value = itemsList.value?.plus("Extra ${itemsList.value?.size?.plus(1)}")
    }

    /*
    private val _text = MutableLiveData<String>().apply {
        value = "This is character stuffs"
    }
    val text: LiveData<String> = _text

     */
}