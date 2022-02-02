package isthisstuff.practice.marvellisimohdd.ui.data

import android.util.Log
import retrofit2.Retrofit
import okhttp3.OkHttpClient
import androidx.lifecycle.ViewModel
import android.annotation.SuppressLint
import io.reactivex.schedulers.Schedulers
import androidx.lifecycle.MutableLiveData
import retrofit2.converter.gson.GsonConverterFactory
import isthisstuff.practice.marvellisimohdd.retrofit.*
import io.reactivex.android.schedulers.AndroidSchedulers
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import isthisstuff.practice.marvellisimohdd.entities.MarvelObject

class MarvelViewModel() : ViewModel() {
    var itemsList = MutableLiveData<List<MarvelObject>>()
    val itemsFound:MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)

    private val builder = OkHttpClient.Builder()
    private val okHttpClient = builder.build()
    private val service: MarvelService = Retrofit.Builder()
        .baseUrl(MARVEL_API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(okHttpClient)/*TODO getOkHttpClient*/
        .build()
        .create(MarvelService::class.java)

        init {
        itemsList.value = listOf()
    }

    @SuppressLint("CheckResult")
    fun getData(dataType:MarvelDatatypes?, offset:Int, vararg arguments: Pair<String, String>) {
        var url:String = MARVEL_API_BASE_URL
        url += when(dataType) {
            MarvelDatatypes.CHARACTERS -> "characters"
            MarvelDatatypes.SERIES -> "series"
            else -> "characters"
        }
        url += "?ts=$ts&apikey=$apiKey&hash=$HASH&limit=50&offset=$offset"
        arguments.forEach {
            url += "&"+it.first+"="+it.second
        }

        service.builtRequestResult(url)!!
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result, err ->
                if (err?.message != null)
                    Log.d("__", "Error getAll " + err.message)
                else {
                    Log.d("__", "I got a DataWrapper $result")

                    result!!.data.results.forEach {
                        itemsList.value = itemsList.value?.plus(it)
                    }
                    if(itemsList.value!!.isEmpty()) itemsFound.value = false
                    if(itemsList.value!!.isNotEmpty()) itemsFound.value = true
                }
            }
    }

    fun clearSearchData() {
        itemsList.value = listOf()
    }
}