package isthisstuff.practice.marvellisimohdd.ui.characters

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
import isthisstuff.practice.marvellisimohdd.R
import isthisstuff.practice.marvellisimohdd.ui.adapter.SearchResultsAdapter

class CharactersFragment : Fragment() {
    private val charactersViewModel: CharactersViewModel by viewModels()
    private var adapter: SearchResultsAdapter = SearchResultsAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        charactersViewModel.itemsList.observe(viewLifecycleOwner, Observer {
            adapter.data = it
        })

        val root = inflater.inflate(R.layout.fragment_characters, container, false)

        root.rootView.findViewById<RecyclerView>(R.id.search_results_characters).adapter = adapter
        root.rootView.findViewById<ImageButton>(R.id.search_button_characters)
            .setOnClickListener { performSearch("%"+root.rootView.findViewById<EditText>(R.id.search_field_characters).text.toString()) }

        return root
    }

    fun performSearch(query:String) {
        charactersViewModel.getData(query)
    }


}