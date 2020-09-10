package isthisstuff.practice.marvellisimohdd.ui.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DetailsViewModel() : ViewModel(){




    init{
        Log.i("DetailsViewModel","DetailsViewModel created!!")
    }
    private val _text = MutableLiveData<String>().apply{
        value = "Test value"
    }
    val text : LiveData<String> = _text


    override fun onCleared() {
        super.onCleared()
        Log.i("DetailsViewModel","DetailsViewModel destroyed! (or, Cleared, rather)")
    }
}