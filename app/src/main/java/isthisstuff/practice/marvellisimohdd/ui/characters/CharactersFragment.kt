package isthisstuff.practice.marvellisimohdd.ui.characters

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
import isthisstuff.practice.marvellisimohdd.R
import isthisstuff.practice.marvellisimohdd.hideKeyboard
import isthisstuff.practice.marvellisimohdd.ui.adapter.SearchResultsAdapter
import isthisstuff.practice.marvellisimohdd.ui.data.MarvelDatatypes
import isthisstuff.practice.marvellisimohdd.ui.data.MarvelViewModel


class CharactersFragment : Fragment() {
    lateinit var root: View
    val marvelViewModel: MarvelViewModel by viewModels()
    private var adapter: SearchResultsAdapter = SearchResultsAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        marvelViewModel.itemsList.observe(viewLifecycleOwner, Observer {
            adapter.data = it
        })

        root = inflater.inflate(R.layout.fragment_characters, container, false)

        root.rootView.findViewById<RecyclerView>(R.id.search_results_characters).adapter = adapter
        root.rootView.findViewById<ImageButton>(R.id.search_button_characters)
            .setOnClickListener { performSearch(root.rootView.findViewById<EditText>(R.id.search_field_characters).text.toString()) }

        root.rootView.findViewById<EditText>(R.id.search_field_characters)
            .setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    performSearch(root.rootView.findViewById<EditText>(R.id.search_field_characters).text.toString())
                    true
                } else false
            }

        return root
    }

    private fun performSearch(query: String, offset: Int = 0) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context)
        var preferredSearchMethod = sharedPreferences.getString("list_search_mode", "")
        Log.d("kolla, såhär har du valt att söka", "$preferredSearchMethod")
        if (preferredSearchMethod == null) {
            preferredSearchMethod = "contains"
        }

        root.rootView.findViewById<EditText>(R.id.search_field_characters).clearFocus()
        hideKeyboard()
        marvelViewModel.clearSearchData()
        marvelViewModel.getData(MarvelDatatypes.CHARACTERS, query, offset, preferredSearchMethod)
        adapter.saveRequestData(
            marvelViewModel,
            MarvelDatatypes.CHARACTERS,
            query,
            offset,
            preferredSearchMethod
        )
    }
}