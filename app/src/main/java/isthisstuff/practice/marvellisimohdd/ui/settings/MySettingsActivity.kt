package isthisstuff.practice.marvellisimohdd.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import isthisstuff.practice.marvellisimohdd.R

class MySettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings_container, MySettingsFragment())
            .commit()
    }
}
