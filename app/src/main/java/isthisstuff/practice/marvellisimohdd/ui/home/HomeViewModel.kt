package isthisstuff.practice.marvellisimohdd.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "MURVEL"
    }
    val text: LiveData<String> = _text
}