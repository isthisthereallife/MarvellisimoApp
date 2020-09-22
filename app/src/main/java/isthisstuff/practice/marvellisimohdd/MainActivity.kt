package isthisstuff.practice.marvellisimohdd

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.realm.Realm
import io.realm.RealmList
import io.realm.kotlin.where
import isthisstuff.practice.marvellisimohdd.database.MarvelRealmObject
import isthisstuff.practice.marvellisimohdd.database.User
import isthisstuff.practice.marvellisimohdd.firebase.MyFirebaseMessagingService
import isthisstuff.practice.marvellisimohdd.ui.settings.MySettingsActivity

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val realm: Realm = Realm.getDefaultInstance()
    private var menuInflated: Boolean = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        //FIREBASE cloud messaging
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener(OnCompleteListener { task ->
            if(!task.isSuccessful){
                Log.w("FAILURE! Tried to get a FirebaseInstanceID.getInstance().instanceID", "getInstanceID failed", task.exception)
                return@OnCompleteListener
            }
            val token = task.result?.token

            val msg = getString(R.string.msg_token_fmt,token)
            Log.d("Got a token","Token is: $msg")
            Toast.makeText(baseContext,"is there anything here?: $msg",Toast.LENGTH_SHORT).show()
        })
        //end of FIREBASE cloud messaging

        //PURGE all null users
        realm.executeTransaction {
            realm.where<User>().isNull("email").findAll().deleteAllFromRealm()
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        //val navHeader = findViewById<>()
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as a top level destination.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_character, R.id.nav_series
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.getHeaderView(0).setOnClickListener { login() }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean{
        val id = item.itemId
        Log.d("onOptionsItemSelected: ","CLICKED ITEM ${item.title}")
        if (id == R.id.action_settings){
            //kicka settings-aktiviteten
            startActivity(Intent(this@MainActivity,MySettingsActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveUser() {
        realm.beginTransaction()
        val newUser = User()
        newUser.email = FirebaseAuth.getInstance().currentUser?.email
        newUser.name = FirebaseAuth.getInstance().currentUser?.displayName
        newUser.favorites = RealmList<MarvelRealmObject>()

        realm.copyToRealmOrUpdate(newUser)
        realm.commitTransaction()

        val user = realm.where<User>().findFirst()
        println("Saved new user: ${user!!.name}")
    }

    override fun onResume() {
        super.onResume()
        val dbUser:User? = realm.where<User>().equalTo("email", FirebaseAuth.getInstance().currentUser?.email).findFirst()
        val activeUser:FirebaseUser? = FirebaseAuth.getInstance().currentUser
        println("onResume, dbUser is: $dbUser\nand activeUser is: $activeUser")
        if(dbUser==null && activeUser!=null) {
            saveUser()
        }
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
        Log.d("onDestroy","FirebaseAuth signed out, realm closed")
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
