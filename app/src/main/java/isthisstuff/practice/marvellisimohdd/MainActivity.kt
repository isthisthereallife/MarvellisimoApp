package isthisstuff.practice.marvellisimohdd

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
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
import isthisstuff.practice.marvellisimohdd.entities.UserViewModel
class MainActivity : AppCompatActivity() {
    val userViewModel: UserViewModel by viewModels()
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        //val activeUser = User("testuser")

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        findViewById<LinearLayout>(R.id.signIn).setOnClickListener { login() }
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun login() {
        super.onStart()
        val fb = FirebaseApp.initializeApp(this)

        val mAuth = FirebaseAuth.getInstance()

        var currentUser = mAuth.getCurrentUser()

        //om är null, vi har inte loggat in
        if (currentUser == null) {

            val intent = AuthUI.getInstance().createSignInIntentBuilder().build()
            startActivityForResult(intent, 1).also {
                changeLogin()
            }
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        FirebaseAuth.getInstance().signOut()
        Toast.makeText(this,"onDestroy", Toast.LENGTH_LONG).show()

    }
    fun changeLogin(){
        findViewById<TextView>(R.id.nameUser).text = FirebaseAuth.getInstance().currentUser?.displayName
        findViewById<TextView>(R.id.emailUser).text = FirebaseAuth.getInstance().currentUser?.email
    }
}
