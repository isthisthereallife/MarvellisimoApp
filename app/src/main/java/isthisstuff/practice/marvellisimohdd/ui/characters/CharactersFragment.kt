package isthisstuff.practice.marvellisimohdd.ui.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import isthisstuff.practice.marvellisimohdd.R
import isthisstuff.practice.marvellisimohdd.retrofit.MARVEL_API_BASE_URL
import isthisstuff.practice.marvellisimohdd.retrofit.MarvelService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class CharactersFragment : Fragment() {
    private val charactersViewModel: CharactersViewModel by viewModels()
    private var adapter:SearchResultsAdapter = SearchResultsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        charactersViewModel.itemsList.observe(viewLifecycleOwner, Observer {
            adapter.data = it
        })

        val root = inflater.inflate(R.layout.fragment_characters, container, false)

        root.rootView.findViewById<RecyclerView>(R.id.search_results).adapter = adapter
        root.rootView.findViewById<ImageButton>(R.id.search_button).setOnClickListener {

            addItem()

             }

        return root
    }

    fun addItem() {
        //charactersViewModel.addItem()
        charactersViewModel.getData()

    }



}