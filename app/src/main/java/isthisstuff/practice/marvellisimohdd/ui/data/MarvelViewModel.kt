package isthisstuff.practice.marvellisimohdd.ui.data

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import isthisstuff.practice.marvellisimohdd.entities.MarvelObject
import isthisstuff.practice.marvellisimohdd.retrofit.MARVEL_API_BASE_URL
import isthisstuff.practice.marvellisimohdd.retrofit.MarvelService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MarvelViewModel:ViewModel() {
    var itemsList = MutableLiveData<List<MarvelObject>>()
    private val builder = OkHttpClient.Builder()
    private val okHttpClient = builder.build()
    val service: MarvelService = Retrofit.Builder()
        .baseUrl(MARVEL_API_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(okHttpClient)/*TODO getOkHttpClient*/
        .build()
        .create(MarvelService::class.java)

    init {
        itemsList.value= listOf()
    }

    fun getData(marvelDatatype: MarvelDatatypes, query:String, offset: Int) {
        //TODO spara ner datan
        when (marvelDatatype) {
            MarvelDatatypes.CHARACTERS -> getCharactersContains(query, offset)
            MarvelDatatypes.SERIES -> getSeriesContains(query, offset)
        }
    }

    @SuppressLint("CheckResult")
    fun getCharactersContains(query: String, offset:Int) {
        service.getCharacterContains(query = "%$query", offset = offset)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result, err ->
                if (err?.message != null)
                    Log.d("__", "Error getAll " + err.message)
                else {
                    Log.d("__", "I got a DataWrapper $result")

                    result.data.results.forEach {
                        itemsList.value = itemsList.value?.plus(it)
                    }
                }
            }
    }

    @SuppressLint("CheckResult")
    fun getSeriesContains(query: String, offset:Int) {
        service.getSeriesContains(query = "%$query", offset = offset)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result, err ->
                if (err?.message != null)
                    Log.d("__", "Error getAll " + err.message)
                else {
                    Log.d("__", "I got a DataWrapper $result")

                    result.data.results.forEach {
                        itemsList.value = itemsList.value?.plus(it)
                    }
                }
            }
    }

    fun clearSearchData() {
        itemsList.value = listOf()
    }
}