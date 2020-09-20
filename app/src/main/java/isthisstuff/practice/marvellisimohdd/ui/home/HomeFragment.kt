package isthisstuff.practice.marvellisimohdd.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import isthisstuff.practice.marvellisimohdd.MainActivity
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