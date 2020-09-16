package isthisstuff.practice.marvellisimohdd.ui.characters

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

class CharactersViewModel : ViewModel() {

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

    fun getData(query:String) {
        //TODO spara ner datan
        getContains(query)
    }

    fun getStartsWith() {
        service.getStartsWith()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result, err ->
                if (err?.message != null)
                    Log.d("__", "Error getAllCharacters " + err.message)
                else {
                    Log.d("__", "I got a CharacterDataWrapper $result")
                    var i = 0
                    while (i < 19) {
                        Log.d(
                            "STARTS WITH",
                            result.data.results[i].name.toString()
                                .plus(": " + result.data.results[i].description)
                        )
                        i++
                    }

                }
            }
    }

    fun getAllCharacters() {
        service.getAllCharacters()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result, err ->
                if (err?.message != null)
                    Log.d("__", "Error getAllCharacters " + err.message)
                else {
                    Log.d("__", "I got a CharacterDataWrapper $result")
                    var i = 0
                    while (i < 19) {
                        Log.d(
                            "Get all characters",
                            result.data.results[i].name.toString()
                                .plus(": " + result.data.results[i].description)
                        )
                        i++
                    }

                }
            }
    }

    @SuppressLint("CheckResult")
    fun getContains(query: String) {
        service.getContains(query = query)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result, err ->
                if (err?.message != null)
                    Log.d("__", "Error getAllCharacters " + err.message)
                else {
                    Log.d("__", "I got a CharacterDataWrapper $result")

                    result.data.results.forEach {
                        itemsList.value = itemsList.value?.plus(it)
                    }
                }
            }
    }


    /*
    private val _text = MutableLiveData<String>().apply {
        value = "This is character stuffs"
    }
    val text: LiveData<String> = _text

     */
}