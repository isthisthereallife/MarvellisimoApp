package isthisstuff.practice.marvellisimohdd.ui.find

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FindViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is find Fragment"
    }
    val text: LiveData<String> = _text
}