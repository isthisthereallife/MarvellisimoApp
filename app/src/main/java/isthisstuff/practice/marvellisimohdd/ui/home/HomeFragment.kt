package isthisstuff.practice.marvellisimohdd.ui.home

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import isthisstuff.practice.marvellisimohdd.R

class HomeFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        root.findViewById<LinearLayout>(R.id.home_linear_layout).setOnClickListener{
            //TODO den här ska göra samma som hamburgaren (öppna sidomenyn)

        }

        return root
    }

}