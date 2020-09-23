package isthisstuff.practice.marvellisimohdd.ui.data

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import isthisstuff.practice.marvellisimohdd.checkFavorite
import isthisstuff.practice.marvellisimohdd.entities.MarvelObject
import isthisstuff.practice.marvellisimohdd.retrofit.MARVEL_API_BASE_URL
import isthisstuff.practice.marvellisimohdd.retrofit.MarvelService
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MarvelViewModel : ViewModel() {
    var itemsList = MutableLiveData<List<MarvelObject>>()
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

    fun getData(
        _marvelDatatype: MarvelDatatypes,
        _query: String,
        _offset: Int,
        preferredSearchMethod: String?,
        onlyFavorites:Boolean = false
    ) {
        when (preferredSearchMethod) {
            "contains" -> {
                when (_marvelDatatype) {
                    MarvelDatatypes.CHARACTERS -> getCharactersContains(_query, _offset, onlyFavorites)
                    MarvelDatatypes.SERIES -> getSeriesContains(_query, _offset, onlyFavorites)
                }
            }
            "startsWith" -> {
                when (_marvelDatatype) {
                    MarvelDatatypes.CHARACTERS -> getCharactersStartsWith(_query, _offset, onlyFavorites)
                    MarvelDatatypes.SERIES -> getSeriesStartsWith(_query, _offset, onlyFavorites)
                }
            }
            "strict" -> {
                when (_marvelDatatype) {
                    MarvelDatatypes.CHARACTERS -> getCharactersStrict(_query, _offset, onlyFavorites)
                    MarvelDatatypes.SERIES -> getSeriesStrict(_query, _offset, onlyFavorites)
                }
            }

        }


    }


    @SuppressLint("CheckResult")
    fun getCharactersContains(_query: String, _offset: Int, onlyFavorites: Boolean = false) {
        service.getCharacterContains(query = "%$_query", offset = _offset)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result, err ->
                if (err?.message != null)
                    Log.d("__", "Error getAll " + err.message)
                else {
                    Log.d("__", "I got a DataWrapper $result")

                    result.data.results.forEach {
                        if(onlyFavorites) {
                            if(checkFavorite(it.id)) {
                                itemsList.value = itemsList.value?.plus(it)
                            }
                        } else {
                            itemsList.value = itemsList.value?.plus(it)
                        }
                    }
                }
            }
    }

    @SuppressLint("CheckResult")
    fun getCharactersStartsWith(_query: String, _offset: Int, onlyFavorites: Boolean = false) {
        service.getCharacterStartsWith(query = _query, offset = _offset)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result, err ->
                if (err?.message != null)
                    Log.d("__", "Error getAll " + err.message)
                else {
                    Log.d("__", "I got a DataWrapper $result")

                    result.data.results.forEach {
                        if(onlyFavorites) {
                            if(checkFavorite(it.id)) {
                                itemsList.value = itemsList.value?.plus(it)
                            }
                        } else {
                            itemsList.value = itemsList.value?.plus(it)
                        }
                    }
                }
            }
    }

    @SuppressLint("CheckResult")
    fun getCharactersStrict(_query: String, _offset: Int, onlyFavorites: Boolean = false) {
        service.getCharacterStrict(query = _query, offset = _offset)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result, err ->
                if (err?.message != null)
                    Log.d("__", "Error getAll " + err.message)
                else {
                    Log.d("__", "I got a DataWrapper $result")

                    result.data.results.forEach {
                        if(onlyFavorites) {
                            if(checkFavorite(it.id)) {
                                itemsList.value = itemsList.value?.plus(it)
                            }
                        } else {
                            itemsList.value = itemsList.value?.plus(it)
                        }
                    }
                }
            }
    }

    @SuppressLint("CheckResult")
    fun getSeriesContains(_query: String, _offset: Int, onlyFavorites: Boolean = false) {
        service.getSeriesContains(query = "%$_query", offset = _offset)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result, err ->
                if (err?.message != null)
                    Log.d("__", "Error getAll " + err.message)
                else {
                    Log.d("__", "I got a DataWrapper $result")

                    result.data.results.forEach {
                        if(onlyFavorites) {
                            if(checkFavorite(it.id)) {
                                itemsList.value = itemsList.value?.plus(it)
                            }
                        } else {
                            itemsList.value = itemsList.value?.plus(it)
                        }
                    }
                }
            }
    }

    @SuppressLint("CheckResult")
    fun getSeriesStartsWith(_query: String, _offset: Int, onlyFavorites: Boolean = false) {
        service.getSeriesStartsWith(query = _query, offset = _offset)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result, err ->
                if (err?.message != null)
                    Log.d("__", "Error getAll " + err.message)
                else {
                    Log.d("__", "I got a DataWrapper $result")

                    result.data.results.forEach {
                        if(onlyFavorites) {
                            if(checkFavorite(it.id)) {
                                itemsList.value = itemsList.value?.plus(it)
                            }
                        } else {
                            itemsList.value = itemsList.value?.plus(it)
                        }
                    }
                }
            }
    }

    @SuppressLint("CheckResult")
    fun getSeriesStrict(_query: String, _offset: Int, onlyFavorites: Boolean = false) {
        service.getSeriesStrict(query = _query, offset = _offset)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result, err ->
                if (err?.message != null)
                    Log.d("__", "Error getAll " + err.message)
                else {
                    Log.d("__", "I got a DataWrapper $result")

                    result.data.results.forEach {
                        if(onlyFavorites) {
                            if(checkFavorite(it.id)) {
                                itemsList.value = itemsList.value?.plus(it)
                            }
                        } else {
                            itemsList.value = itemsList.value?.plus(it)
                        }
                    }
                }
            }
    }


    @SuppressLint("CheckResult")
    fun getSeriesContainingCharacter(_query: String, _offset: Int, onlyFavorites: Boolean = false) {
        service.getSeriesContainingCharacter(query = _query, offset = _offset)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result, err ->
                if (err?.message != null)
                    Log.d("__", "Error getAll " + err.message)
                else {
                    Log.d("__", "I got a DataWrapper $result")

                    result.data.results.forEach {
                        if(onlyFavorites) {
                            if(checkFavorite(it.id)) {
                                itemsList.value = itemsList.value?.plus(it)
                            }
                        } else {
                            itemsList.value = itemsList.value?.plus(it)
                        }
                    }
                }
            }
    }

    @SuppressLint("CheckResult")
    fun getCharactersInSeries(_query: String, _offset: Int, onlyFavorites: Boolean = false) {
        service.getCharactersInSeries(query = _query, offset = _offset)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { result, err ->
                if (err?.message != null)
                    Log.d("__", "Error getAll " + err.message)
                else {
                    Log.d("__", "I got a DataWrapper $result")

                    result.data.results.forEach {
                        if(onlyFavorites) {
                            if(checkFavorite(it.id)) {
                                itemsList.value = itemsList.value?.plus(it)
                            }
                        } else {
                            itemsList.value = itemsList.value?.plus(it)
                        }
                    }
                }
            }
    }

    fun clearSearchData() {
        itemsList.value = listOf()
    }
}