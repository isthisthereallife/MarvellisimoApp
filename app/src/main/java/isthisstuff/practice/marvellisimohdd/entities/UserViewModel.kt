package isthisstuff.practice.marvellisimohdd.entities

import android.util.Log
import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel(){
    init{
        Log.i("UserViewModel","USER VIEW MODEL CREATED!")
    }



    override fun onCleared(){
        super.onCleared()
        Log.i("UserViewModel", "UserViewModel destroyed!!!")
    }
}