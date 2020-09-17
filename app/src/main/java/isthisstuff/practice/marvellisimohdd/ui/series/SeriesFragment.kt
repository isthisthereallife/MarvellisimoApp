package isthisstuff.practice.marvellisimohdd.ui.series

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import isthisstuff.practice.marvellisimohdd.ui.data.MarvelDatatypes
import isthisstuff.practice.marvellisimohdd.R
import isthisstuff.practice.marvellisimohdd.ui.data.MarvelViewModel
import isthisstuff.practice.marvellisimohdd.ui.adapter.SearchResultsAdapter

class SeriesFragment : Fragment() {

    private val marvelViewModel: MarvelViewModel by viewModels()
    private var adapter:SearchResultsAdapter = SearchResultsAdapter(this)

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        marvelViewModel.itemsList.observe(viewLifecycleOwner, Observer {
            adapter.data = it
        })

        val root = inflater.inflate(R.layout.fragment_series, container, false)

        root.rootView.findViewById<RecyclerView>(R.id.search_results_series).adapter = adapter
        root.rootView.findViewById<ImageButton>(R.id.search_button_series)
            .setOnClickListener { performSearch("%"+root.rootView.findViewById<EditText>(R.id.search_field_series).text.toString()) }

        return root
    }

    private fun performSearch(query:String, offset:Int = 0) {
        marvelViewModel.clearSearchData()
        marvelViewModel.getData(MarvelDatatypes.SERIES, query, offset)
        adapter.saveRequestData(marvelViewModel, MarvelDatatypes.SERIES, query, offset)
    }
}