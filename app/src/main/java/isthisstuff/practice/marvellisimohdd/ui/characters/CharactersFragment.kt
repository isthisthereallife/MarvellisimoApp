package isthisstuff.practice.marvellisimohdd.ui.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import isthisstuff.practice.marvellisimohdd.R

class CharactersFragment : Fragment() {
    private lateinit var charactersViewModel: CharactersViewModel
    lateinit var adapter:SearchResultsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        charactersViewModel = ViewModelProvider(this).get(CharactersViewModel::class.java)
        charactersViewModel.itemsList.observe(viewLifecycleOwner, Observer {
            adapter.data = it
        })

        val root = inflater.inflate(R.layout.fragment_characters, container, false)

        adapter = SearchResultsAdapter()
        root.rootView.findViewById<RecyclerView>(R.id.search_results).adapter = adapter
        root.rootView.findViewById<ImageButton>(R.id.search_button).setOnClickListener {addItem()}

        return root
    }

    fun addItem() {
        charactersViewModel.addItem()
    }
}