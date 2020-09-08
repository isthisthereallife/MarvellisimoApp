package isthisstuff.practice.marvellisimohdd.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DetailsViewModel : ViewModel(){

    private val _text = MutableLiveData<String>().apply{
        value = "Test value"
    }
    val text : LiveData<String> = _text
}