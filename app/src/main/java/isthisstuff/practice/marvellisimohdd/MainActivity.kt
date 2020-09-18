package isthisstuff.practice.marvellisimohdd

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.firebase.ui.auth.AuthUI
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import io.realm.Realm
import io.realm.RealmList
import io.realm.kotlin.where
import isthisstuff.practice.marvellisimohdd.database.MarvelRealmObject
import isthisstuff.practice.marvellisimohdd.database.User

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var realm: Realm
    private var meny: Unit? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        //REALM
        realm = Realm.getDefaultInstance()


        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as a top level destination.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_character, R.id.nav_series
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


    }

    private fun saveUser() {

        realm.beginTransaction()
        val user1 = User()
        user1.email = FirebaseAuth.getInstance().currentUser?.email
        user1.name = FirebaseAuth.getInstance().currentUser?.displayName
        user1.favorites = RealmList<MarvelRealmObject>()

        realm.copyToRealmOrUpdate(user1)
        realm.commitTransaction()

        val result = realm.where<User>().findAll()
        Log.d("HÄMTAT EN USER!!!!", result.toString())

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        meny = menuInflater.inflate(R.menu.main, menu)
        findViewById<LinearLayout>(R.id.signIn).setOnClickListener { login() }
        updateLoginDisplay()
        saveUser()
        return true
    }

    override fun onResume() {
        super.onResume()
        if (meny != null)
            updateLoginDisplay()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun login() {
        FirebaseApp.initializeApp(this)
        val mAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
        if (currentUser == null) {
            val intent = AuthUI.getInstance().createSignInIntentBuilder().build()
            startActivityForResult(intent, 1)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        FirebaseAuth.getInstance().signOut()
        realm.close()
    }

    private fun updateLoginDisplay() {
        if (FirebaseAuth.getInstance().currentUser != null) {
            findViewById<TextView>(R.id.nameUser).text =
                FirebaseAuth.getInstance().currentUser?.displayName
            findViewById<TextView>(R.id.emailUser).text =
                FirebaseAuth.getInstance().currentUser?.email
        }
    }

}
