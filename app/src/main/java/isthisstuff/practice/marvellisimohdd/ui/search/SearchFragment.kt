package isthisstuff.practice.marvellisimohdd.ui.search

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import io.realm.Case
import io.realm.Realm
import io.realm.kotlin.where
import isthisstuff.practice.marvellisimohdd.*
import isthisstuff.practice.marvellisimohdd.database.MarvelRealmObject
import isthisstuff.practice.marvellisimohdd.entities.MarvelObject
import isthisstuff.practice.marvellisimohdd.ui.adapter.SearchResultsAdapter
import isthisstuff.practice.marvellisimohdd.ui.data.MarvelDatatypes
import isthisstuff.practice.marvellisimohdd.ui.data.MarvelViewModel

class SearchFragment : Fragment() {
    lateinit var root: View

    private var realm = Realm.getDefaultInstance()
    private val marvelViewModel: MarvelViewModel by viewModels()
    private var searchResultsAdapter: SearchResultsAdapter = SearchResultsAdapter(this)
    private lateinit var sharedPreferences:SharedPreferences

    var query:String = ""
    var dataType:MarvelDatatypes = MarvelDatatypes.CHARACTERS
    private var searchMethod:String = "contains"
    var onlyFavorites:Boolean = false

    private lateinit var recyclerView:RecyclerView
    private lateinit var searchButton:ImageButton
    private lateinit var inputField:EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_search, container, false)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context)

        searchMethod = sharedPreferences.getString("list_search_mode", "contains").toString()

        marvelViewModel.itemsList.observe(viewLifecycleOwner, {
            searchResultsAdapter.data = it
        })

        recyclerView = root.findViewById(R.id.search_results)
        searchButton = root.findViewById(R.id.search_button)
        inputField = root.findViewById(R.id.search_field)

        recyclerView.adapter = searchResultsAdapter

        val incomingData = this.arguments

        searchButton.setOnClickListener {
            updateDataTypeByTitle()
            searchMethod = sharedPreferences.getString("list_search_mode", "contains").toString()
            runSearch(inputField.text.toString(), dataType)
        }

        inputField.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchMethod = sharedPreferences.getString("list_search_mode", "contains").toString()
                runSearch(inputField.text.toString(), dataType)
                true
            } else false
        }

        updateDataTypeByTitle()
        updateTitleAndHintByDataType(dataType)

        if(incomingData!=null) {
            searchMethod = "related"
            query = incomingData.get("query") as String
            dataType = incomingData.get("dataType") as MarvelDatatypes
            runSearch(query, dataType)
        }

        return root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(resultCode) {
            1 -> { // success!
                searchMethod = "related"
                query = data!!.extras!!.get("query") as String
                dataType = data.extras!!.get("dataType") as MarvelDatatypes
                updateTitleAndHintByDataType(dataType)
                runSearch(query, dataType)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if(searchMethod!="related") {
            runSearch(query, dataType)
        }
    }

    fun runSearch(query:String, dataType: MarvelDatatypes?, offset: Int = 0, cleanSlate:Boolean = true) {
        // prepare for search
        hideKeyboard()
        inputField.clearFocus()

        if(cleanSlate) {
            searchResultsAdapter.resetOffset()
            marvelViewModel.clearSearchData()
        }

        onlyFavorites = sharedPreferences.getBoolean("only_favorites", false)
        this.query = query

        // return api results filtered by the search
        when(searchMethod) {
            "contains" -> {
                when(dataType) {
                    MarvelDatatypes.CHARACTERS -> {
                        if(onlyFavorites || !isOnline(context)) {
                            var results:List<MarvelObject> = listOf()
                            realm.where<MarvelRealmObject>().contains("name", query, Case.INSENSITIVE).findAll().forEach {
                                results = results.plus(convertMarvelRealmObjectToMarvelObject(it))
                            }
                            if(onlyFavorites) {
                                var favoriteResults:List<MarvelObject> = listOf()
                                results.forEach {
                                    if(checkFavorite(it.id)) favoriteResults = favoriteResults.plus(it)
                                }
                                results = favoriteResults
                            }
                            results.forEach {
                                marvelViewModel.itemsList.value = results
                            }
                        } else marvelViewModel.getData(dataType, offset, Pair("nameStartsWith", "%$query"))
                    }
                    MarvelDatatypes.SERIES -> {
                        if(onlyFavorites || !isOnline(context)) {
                            var results:List<MarvelObject> = listOf()
                            realm.where<MarvelRealmObject>().contains("title", query, Case.INSENSITIVE).findAll().forEach {
                                results = results.plus(convertMarvelRealmObjectToMarvelObject(it))
                            }
                            if(onlyFavorites) {
                                var favoriteResults:List<MarvelObject> = listOf()
                                results.forEach {
                                    if(checkFavorite(it.id)) favoriteResults = favoriteResults.plus(it)
                                }
                                results = favoriteResults
                            }
                            marvelViewModel.itemsList.value = results
                        } else marvelViewModel.getData(dataType, offset, Pair("titleStartsWith", "%$query"))
                    }
                }
            }
            "startsWith" ->  {
                when(dataType) {
                    MarvelDatatypes.CHARACTERS -> {
                        if(onlyFavorites || !isOnline(context)) {
                            var results:List<MarvelObject> = listOf()
                            realm.where<MarvelRealmObject>().beginsWith("name", query, Case.INSENSITIVE).findAll().forEach {
                                results = results.plus(convertMarvelRealmObjectToMarvelObject(it))
                            }
                            if(onlyFavorites) {
                                var favoriteResults:List<MarvelObject> = listOf()
                                results.forEach {
                                    if(checkFavorite(it.id)) favoriteResults = favoriteResults.plus(it)
                                }
                                results = favoriteResults
                            }
                            marvelViewModel.itemsList.value = results
                        } else marvelViewModel.getData(dataType, offset, Pair("nameStartsWith", query))
                    }
                    MarvelDatatypes.SERIES -> {
                        if(onlyFavorites || !isOnline(context)) {
                            var results:List<MarvelObject> = listOf()
                            realm.where<MarvelRealmObject>().beginsWith("name", query, Case.INSENSITIVE).findAll().forEach {
                                results = results.plus(convertMarvelRealmObjectToMarvelObject(it))
                            }
                            if(onlyFavorites) {
                                var favoriteResults:List<MarvelObject> = listOf()
                                results.forEach {
                                    if(checkFavorite(it.id)) favoriteResults = favoriteResults.plus(it)
                                }
                                results = favoriteResults
                            }
                            marvelViewModel.itemsList.value = results
                        } else marvelViewModel.getData(dataType, offset, Pair("titleStartsWith", query))
                    }
                }
            }
            "strict" -> {
                when(dataType) {
                    MarvelDatatypes.CHARACTERS -> {
                        if(onlyFavorites || !isOnline(context)) {
                            var results:List<MarvelObject> = listOf()
                            realm.where<MarvelRealmObject>().equalTo("name", query, Case.INSENSITIVE).findAll().forEach {
                                results = results.plus(convertMarvelRealmObjectToMarvelObject(it))
                            }
                            if(onlyFavorites) {
                                var favoriteResults:List<MarvelObject> = listOf()
                                results.forEach {
                                    if(checkFavorite(it.id)) favoriteResults = favoriteResults.plus(it)
                                }
                                results = favoriteResults
                            }
                            marvelViewModel.itemsList.value = results
                        } else marvelViewModel.getData(dataType, offset, Pair("name", query))
                    }
                    MarvelDatatypes.SERIES -> {
                        if(onlyFavorites || !isOnline(context)) {
                            var results:List<MarvelObject> = listOf()
                            realm.where<MarvelRealmObject>().equalTo("name", query, Case.INSENSITIVE).findAll().forEach {
                                results = results.plus(convertMarvelRealmObjectToMarvelObject(it))
                            }
                            if(onlyFavorites) {
                                var favoriteResults:List<MarvelObject> = listOf()
                                results.forEach {
                                    if(checkFavorite(it.id)) favoriteResults = favoriteResults.plus(it)
                                }
                                results = favoriteResults
                            }
                            marvelViewModel.itemsList.value = results
                        } else marvelViewModel.getData(dataType, offset, Pair("title", query))
                    }
                }
            }
            "related" -> {
                when(dataType) {
                    MarvelDatatypes.CHARACTERS -> {
                        if(!onlyFavorites && isOnline(context)) marvelViewModel.getData(dataType, offset, Pair("series", query))
                    }
                    MarvelDatatypes.SERIES -> {
                        if(!onlyFavorites && isOnline(context)) marvelViewModel.getData(dataType, offset, Pair("characters", query))
                    }
                }
            }
        }
    }

    private fun updateTitleAndHintByDataType(dataType: MarvelDatatypes) {
        when(dataType) {
            MarvelDatatypes.CHARACTERS -> {
                (activity as AppCompatActivity).supportActionBar!!.title = getString(R.string.action_bar_title_characters)
                inputField.hint = getString(R.string.search_hint_character)
            }
            MarvelDatatypes.SERIES -> {
                (activity as AppCompatActivity).supportActionBar!!.title = getString(R.string.action_bar_title_series)
                inputField.hint = getString(R.string.search_hint_series)
            }
        }
    }

    private fun updateDataTypeByTitle() {
        when((activity as AppCompatActivity).supportActionBar!!.title.toString()) {
            "Characters" -> {
                dataType = MarvelDatatypes.CHARACTERS
            }
            "Series" -> {
                dataType = MarvelDatatypes.SERIES
            }
        }
    }
}