package isthisstuff.practice.marvellisimohdd.ui.activeusers

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import isthisstuff.practice.marvellisimohdd.R

class ActiveUsersActivity() : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_active_users)
        Log.d("ActiveUsersActivity", "onCreate, Slutet av.")
    }
}