package isthisstuff.practice.marvellisimohdd.ui.settings

import android.util.Log
import android.os.Bundle
import isthisstuff.practice.marvellisimohdd.R
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat


//basically just a container for the PreferenceFragmentCompat-class MySettingsFragment
class MySettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("INNE I MySettingsActivity: ","Nu ska vi försöka köra fragmentet!")

        setTheme(R.style.SettingStyle)

        supportFragmentManager
            .beginTransaction()
            .replace(android.R.id.content, MySettingsFragment())
            .commit()
    }
   /* VALFRI *
      override fun onPreferenceStartFragment(caller:PreferenceFragmentCompat,
        pref:Preference): Boolean{
        // när en setting som öppnar ett fragment med mer settings klickas så körs den här
        //hantera fancy pantsy transitions / animeringar mellan dem häri
    }*/
}

class MySettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        Log.d("Inne i fragmentet!","this is wonderful! men funkar det?")

        //put preference configurations in here!
    }

   /* override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_preferences, container, false)
        return root
    }*/
}
