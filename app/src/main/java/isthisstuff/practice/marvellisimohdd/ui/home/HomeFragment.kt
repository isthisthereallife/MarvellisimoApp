package isthisstuff.practice.marvellisimohdd.ui.home

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import isthisstuff.practice.marvellisimohdd.R

class HomeFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        root.findViewById<TextView>(R.id.home_characters).setOnClickListener{ Navigation.findNavController(root).navigate(R.id.nav_character, null) }
        root.findViewById<TextView>(R.id.home_series).setOnClickListener{ Navigation.findNavController(root).navigate(R.id.nav_series, null) }

        return root
    }
}