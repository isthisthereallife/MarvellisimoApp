package isthisstuff.practice.marvellisimohdd.ui.series

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import isthisstuff.practice.marvellisimohdd.R
import isthisstuff.practice.marvellisimohdd.ui.adapter.SearchResultsAdapter

class SeriesFragment : Fragment() {

    private val seriesViewModel: SeriesViewModel by viewModels()
    private var adapter:SearchResultsAdapter = SearchResultsAdapter(this)

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        seriesViewModel.itemsList.observe(viewLifecycleOwner, Observer {
            adapter.data = it
        })

        val root = inflater.inflate(R.layout.fragment_series, container, false)

        root.rootView.findViewById<RecyclerView>(R.id.search_results_series).adapter = adapter
        root.rootView.findViewById<ImageButton>(R.id.search_button_series)
            .setOnClickListener { performSearch("%"+root.rootView.findViewById<EditText>(R.id.search_field_series).text.toString()) }

        return root
    }

    fun performSearch(query:String) {
        seriesViewModel.getData(query)
    }
}