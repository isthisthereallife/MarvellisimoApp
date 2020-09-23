package isthisstuff.practice.marvellisimohdd.ui.recommendations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import isthisstuff.practice.marvellisimohdd.R
import isthisstuff.practice.marvellisimohdd.ui.adapter.SearchResultsAdapter
import isthisstuff.practice.marvellisimohdd.ui.data.MarvelViewModel

class RecommendationsFragment :Fragment() {


    class RecommendationsFragment : Fragment() {
        lateinit var root: View
        val marvelViewModel: MarvelViewModel by viewModels()
        private var adapter: SearchResultsAdapter = SearchResultsAdapter(this)

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {

            root = inflater.inflate(R.layout.fragment_recommendations, container, false)
            return root
        }
    }
}