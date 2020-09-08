package isthisstuff.practice.marvellisimohdd.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import isthisstuff.practice.marvellisimohdd.R

class DetailsFragment : Fragment(){
    private lateinit var detailsViewModel: DetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        detailsViewModel = ViewModelProviders.of(this).get(DetailsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_details, container,false)
        val textView: TextView = root.findViewById(R.id.text_details)
        detailsViewModel.text.observe(viewLifecycleOwner, Observer{textView.text = it})
        return root
    }
}