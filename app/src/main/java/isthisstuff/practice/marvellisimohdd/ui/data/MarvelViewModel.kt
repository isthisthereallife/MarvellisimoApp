package isthisstuff.practice.marvellisimohdd.ui.data

import android.annotation.SuppressLint
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import isthisstuff.practice.marvellisimohdd.entities.MarvelObject
import isthisstuff.practice.marvellisimohdd.retrofit.*
import isthisstuff.practice.marvellisimohdd.ui.adapter.SearchResultsAdapter
import isthisstuff.practice.marvellisimohdd.ui.search.SearchFragment
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MarvelViewModel() : ViewModel() {
    var itemsList = MutableLiveData<List<MarvelObject>>()
    val itemsListChanged:MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)

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
                    itemsListChanged.value = !itemsListChanged.value!!
                }
            }
    }

    fun clearSearchData() {
        itemsList.value = listOf()
    }
}