package isthisstuff.practice.marvellisimohdd.ui.characters

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CharactersViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Theeeeeeeeeeeeeeeeeeeeeeeeeeeeent"
    }
    val text: LiveData<String> = _text
}