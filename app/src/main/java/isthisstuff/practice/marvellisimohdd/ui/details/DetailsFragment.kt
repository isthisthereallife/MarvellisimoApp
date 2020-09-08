package isthisstuff.practice.marvellisimohdd.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import isthisstuff.practice.marvellisimohdd.R

class DetailsFragment : Fragment() {
    private lateinit var detailsViewModel: DetailsViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        detailsViewModel = ViewModelProviders.of(this).get(DetailsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_details, container, false)
        val textView: TextView = root.findViewById(R.id.text_details)
        detailsViewModel.text.observe(viewLifecycleOwner, Observer { textView.text = it })

        /* TODO begripa varför denna kod inte verkar köras, nånsin!!!!
                sedan,
                    lista ut om denna ens ska vara här!
        val star : ImageView = root.findViewById(R.id.details_favstar)
        star.setOnClickListener({changeStar(star)})
        */
        return root
    }

    /*TODO
    private fun changeStar(star: ImageView?) {
        if (star != null) {
            when (star.tag) {
                "Filled" -> {
                    star.setImageResource(R.drawable.ic_baseline_star_border_24)
                    star.setTag("Bordered")
                }
                else -> {
                    star.setImageResource(R.drawable.ic_baseline_star_filled_24)
                    star.setTag("Filled")
                }
            }
        }
    }
    */
}