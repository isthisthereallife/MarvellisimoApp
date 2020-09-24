package isthisstuff.practice.marvellisimohdd.ui.search

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
    lateinit var sharedPreferences:SharedPreferences
    var onlyFavorites:Boolean = false
    var preferredSearchMethod:String = "contains"
    val marvelViewModel: MarvelViewModel by viewModels()
    var searchingFor: MarvelDatatypes = MarvelDatatypes.CHARACTERS
    private var adapter: SearchResultsAdapter = SearchResultsAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context)
        onlyFavorites = sharedPreferences.getBoolean("only_favorites", false)

        marvelViewModel.itemsList.observe(viewLifecycleOwner, {
            adapter.data = it
        })

        root = inflater.inflate(R.layout.fragment_search, container, false)

        root.rootView.findViewById<RecyclerView>(R.id.search_results).adapter = adapter
        root.rootView.findViewById<ImageButton>(R.id.search_button)
            .setOnClickListener { performSearch(
                root.rootView.findViewById<EditText>(R.id.search_field).text.toString(),
                0
            ) }
        root.rootView.findViewById<EditText>(R.id.search_field)
            .setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    performSearch(
                        root.rootView.findViewById<EditText>(R.id.search_field).text.toString(),
                        0
                    )
                    true
                } else false
            }
        val searchFieldView:EditText = root.rootView.findViewById<EditText>(R.id.search_field)

        when((activity as AppCompatActivity).supportActionBar!!.title.toString()) {
            "Characters" -> {
                searchingFor = MarvelDatatypes.CHARACTERS
                searchFieldView.hint = getString(R.string.search_hint_character)
            }
            "Series" -> {
                searchingFor = MarvelDatatypes.SERIES
                searchFieldView.hint= getString(R.string.search_hint_series)
            }
        }

        Log.d("debug_print","[Fragment titel: "+(activity as AppCompatActivity).supportActionBar!!.title.toString()+", Sökning efter: "+searchingFor+"]")

        //för att det inte ska se så tomt ut
        performSearch(query = "a")

        return root
    }

    override fun onResume() {
        super.onResume()
        onlyFavorites = sharedPreferences.getBoolean("only_favorites", false)
        if(onlyFavorites) {
            var newList: List<MarvelObject>? = listOf<MarvelObject>()
            realm.where<MarvelRealmObject>().findAll().forEach {
                var convertedObject = convertMarvelRealmObjectToMarvelObject(it)
                if(checkFavorite(convertedObject.id)) {
                    newList = newList!!.plus(convertedObject)
                }
            }
            marvelViewModel.itemsList.value = newList
        } else {
            performSearch(query = "a")
        }
        adapter.notifyDataSetChanged()
    }

    fun performSearch(query: String, offset: Int = 0) {
        Log.d("debug_print", "onlyFavorites = $onlyFavorites")
        preferredSearchMethod = sharedPreferences.getString("list_search_mode", "contains").toString()
        Log.d("kolla, såhär har du valt att söka", preferredSearchMethod)

        root.rootView.findViewById<EditText>(R.id.search_field).clearFocus()
        hideKeyboard()
        marvelViewModel.clearSearchData()

        if(onlyFavorites || airplaneModeIsOn(this.context)) {

            // nu laddar vi från cache istället

            var newList: List<MarvelObject>? = listOf<MarvelObject>()
            realm.where<MarvelRealmObject>().findAll().forEach {
                var convertedObject = convertMarvelRealmObjectToMarvelObject(it)
                newList = newList!!.plus(convertedObject)
            }

            if(onlyFavorites) {
                var newerList: List<MarvelObject>? = listOf<MarvelObject>()
                newList!!.forEach {
                    if(checkFavorite(it.id)) {
                        newerList = newerList!!.plus(it)
                    }
                }
                marvelViewModel.itemsList.value = newerList
            } else {
                marvelViewModel.itemsList.value = newList
            }
        } else {
            marvelViewModel.getData(searchingFor, query, offset, preferredSearchMethod)
        }

        adapter.saveRequestData(
            marvelViewModel,
            searchingFor,
            query,
            offset,
            preferredSearchMethod
        )
    }
}