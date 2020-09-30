package isthisstuff.practice.marvellisimohdd.ui.activeusers

import android.util.Log
import android.os.Bundle
import isthisstuff.practice.marvellisimohdd.R
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import isthisstuff.practice.marvellisimohdd.entities.MarvelObject
import isthisstuff.practice.marvellisimohdd.ui.adapter.ActiveUsersAdapter

class ActiveUsersActivity() : AppCompatActivity() {
    lateinit var marvelObject: MarvelObject
    private var senderEmail: String = ""
    private var database = FirebaseDatabase.getInstance()
    private var databaseCurrentUsersReference = database.getReference("currentUsers")
    lateinit var adapter: ActiveUsersAdapter
    private var boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_active_users)

        marvelObject = intent.extras?.get("MarvelObject") as MarvelObject
        senderEmail = intent.extras?.get("senderEmail") as String
        adapter = ActiveUsersAdapter(senderEmail, marvelObject)
        findViewById<RecyclerView>(R.id.active_user_recyclerView).adapter = adapter


        //FIREBASE database
        val userListener = object : ValueEventListener {
            var concurrentUsersHashMap = HashMap<String, String>()

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.value

                if (user != null) {
                    concurrentUsersHashMap = user as HashMap<String, String>
                    val usersList = concurrentUsersHashMap.toList()
                    val noDoubles = mutableListOf<String>()

                    usersList.forEach {

                        if (noDoubles.contains(it.first)) {
                        } else {
                            noDoubles.add(it.first)
                        }
                    }
                    adapter.data = noDoubles
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(
                    "userListener -> onCancelled",
                    databaseError.toException().printStackTrace().toString()
                )
                //do something here? nah
            }
        }
        databaseCurrentUsersReference.addValueEventListener(userListener)


    }
}
