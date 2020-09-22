package isthisstuff.practice.marvellisimohdd.ui.activeusers

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import isthisstuff.practice.marvellisimohdd.entities.MarvelObject
import isthisstuff.practice.marvellisimohdd.retrofit.MARVEL_API_BASE_URL
import isthisstuff.practice.marvellisimohdd.retrofit.MarvelService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ActiveUsersViewModel : ViewModel() {
    var itemsList = MutableLiveData<List<String>>()
    private val builder = OkHttpClient.Builder()
    private val okHttpClient = builder.build()

    init {
        itemsList.value = listOf()

        //FIREBASE database
        val userListener = object : ValueEventListener {
            var concurrentUsersHashMap = HashMap<String,String>()

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.value
                //wth is is this
                Log.d("userListener -> onDataChange -> dataSnapshot.value", user.toString())

                if (user != null) {
                    concurrentUsersHashMap = user as HashMap<String, String>
                    Log.d("CURRENT USERS", concurrentUsersHashMap.toString())

                    concurrentUsersHashMap.forEach{it ->
                        itemsList.value = itemsList.value?.plus(it.value)
                    }
                }
            }



            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("userListener -> onCancelled", databaseError.toException().printStackTrace().toString())
                //do something here?
            }
        }
    }

    fun getData(
        eMail: String?,
    ) {
        Log.d("ActiveUsersViewModel getData","borde ngt hända här kanske? hmm")
    }



    fun clearSearchData() {
        itemsList.value = listOf()
    }
}