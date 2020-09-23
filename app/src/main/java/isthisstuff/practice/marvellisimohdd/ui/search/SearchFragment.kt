package isthisstuff.practice.marvellisimohdd.ui.search

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
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import isthisstuff.practice.marvellisimohdd.R
import isthisstuff.practice.marvellisimohdd.hideKeyboard
import isthisstuff.practice.marvellisimohdd.ui.adapter.SearchResultsAdapter
import isthisstuff.practice.marvellisimohdd.ui.data.MarvelDatatypes
import isthisstuff.practice.marvellisimohdd.ui.data.MarvelViewModel


class SearchFragment : Fragment() {
    lateinit var root: View
    val marvelViewModel: MarvelViewModel by viewModels()
    private var adapter: SearchResultsAdapter = SearchResultsAdapter(this)
    var searchingFor: MarvelDatatypes = MarvelDatatypes.CHARACTERS

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        marvelViewModel.itemsList.observe(viewLifecycleOwner, Observer {
            adapter.data = it
        })

        root = inflater.inflate(R.layout.fragment_search, container, false)

        root.rootView.findViewById<RecyclerView>(R.id.search_results).adapter = adapter
        root.rootView.findViewById<ImageButton>(R.id.search_button)
            .setOnClickListener { performSearch(root.rootView.findViewById<EditText>(R.id.search_field).text.toString(), 0, searchingFor) }
        root.rootView.findViewById<EditText>(R.id.search_field)
            .setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    performSearch(root.rootView.findViewById<EditText>(R.id.search_field).text.toString(), 0, searchingFor)
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
        performSearch(query = "a", dataType =  searchingFor)

        return root
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }

    fun performSearch(query: String, offset: Int = 0, dataType:MarvelDatatypes=MarvelDatatypes.CHARACTERS) {

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context)
        var preferredSearchMethod = sharedPreferences.getString("list_search_mode", "")
        Log.d("kolla, såhär har du valt att söka", "$preferredSearchMethod")
        if (preferredSearchMethod == null) {
            preferredSearchMethod = "contains"
        }

        root.rootView.findViewById<EditText>(R.id.search_field).clearFocus()
        hideKeyboard()
        marvelViewModel.clearSearchData()
        marvelViewModel.getData(searchingFor, query, offset, preferredSearchMethod)
        adapter.saveRequestData(
            marvelViewModel,
            searchingFor,
            query,
            offset,
            preferredSearchMethod
        )
    }
}