package isthisstuff.practice.marvellisimohdd.ui.home

import android.view.*
import android.net.Uri
import android.os.Bundle
import android.content.Intent
import android.widget.TextView
import android.widget.ImageView
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
        root.findViewById<ImageView>(R.id.copyleft).setOnClickListener{startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.copyleft.org")))}
        root.findViewById<TextView>(R.id.link_daniel).setOnClickListener{startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Dafali9012")))}
        root.findViewById<TextView>(R.id.link_magnus).setOnClickListener{startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/isthisthereallife")))}
        root.findViewById<TextView>(R.id.link_david).setOnClickListener{startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/kitqeb")))}


        return root
    }
}