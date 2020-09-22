package isthisstuff.practice.marvellisimohdd.ui.series

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import isthisstuff.practice.marvellisimohdd.ui.data.MarvelDatatypes
import isthisstuff.practice.marvellisimohdd.R
import isthisstuff.practice.marvellisimohdd.hideKeyboard
import isthisstuff.practice.marvellisimohdd.ui.data.MarvelViewModel
import isthisstuff.practice.marvellisimohdd.ui.adapter.SearchResultsAdapter

class SeriesFragment : Fragment() {

    lateinit var root: View
    private val marvelViewModel: MarvelViewModel by viewModels()
    private var adapter: SearchResultsAdapter = SearchResultsAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        marvelViewModel.itemsList.observe(viewLifecycleOwner, {
            adapter.data = it
        })

        root = inflater.inflate(R.layout.fragment_series, container, false)

        root.rootView.findViewById<RecyclerView>(R.id.search_results_series).adapter = adapter
        root.rootView.findViewById<ImageButton>(R.id.search_button_series)
            .setOnClickListener { performSearch(root.rootView.findViewById<EditText>(R.id.search_field_series).text.toString()) }

        root.rootView.findViewById<EditText>(R.id.search_field_series)
            .setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    performSearch(root.rootView.findViewById<EditText>(R.id.search_field_series).text.toString())
                    true
                } else false
            }

        //för att det inte ska se så tomt ut
        performSearch("a",0)
        return root
    }

    fun performSearch(query: String, offset: Int = 0) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context)
        var preferredSearchMethod = sharedPreferences.getString("list_search_mode", "")
        Log.d("kolla, såhär har du valt att söka", "$preferredSearchMethod")
        if (preferredSearchMethod == null) {
            preferredSearchMethod = "contains"
        }

        root.rootView.findViewById<EditText>(R.id.search_field_series).clearFocus()
        hideKeyboard()
        marvelViewModel.clearSearchData()
        marvelViewModel.getData(MarvelDatatypes.SERIES, query, offset, preferredSearchMethod)
        adapter.saveRequestData(
            marvelViewModel,
            MarvelDatatypes.SERIES,
            query,
            offset,
            preferredSearchMethod
        )
    }
}