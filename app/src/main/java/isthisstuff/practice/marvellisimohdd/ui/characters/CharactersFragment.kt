package isthisstuff.practice.marvellisimohdd.ui.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import isthisstuff.practice.marvellisimohdd.R

class CharactersFragment : Fragment() {
    private lateinit var charactersViewModel: CharactersViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        charactersViewModel = ViewModelProviders.of(this).get(CharactersViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_characters, container, false)

        val adapter = SearchResultsAdapter()
        view?.findViewById<RecyclerView>(R.id.search_results)?.adapter = adapter

        adapter.data = listOf("Daniel", "Magnus", "David", "Janis", "Joacim", "Alexandra", "Alexander", "Sofia")

        //return root
        return root
    }
}