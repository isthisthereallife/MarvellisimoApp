package isthisstuff.practice.marvellisimohdd.ui.activeusers

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import isthisstuff.practice.marvellisimohdd.R
import isthisstuff.practice.marvellisimohdd.ui.adapter.ActiveUsersAdapter

class ActiveUsersFragment : Fragment() {
    lateinit var root: View
    var concurrentUsersHashMap = HashMap<String, String>()
    private var adapter: ActiveUsersAdapter = ActiveUsersAdapter()

    init {
        //FIREBASE database
        val userListener = object : ValueEventListener {
            var concurrentUsersHashMap = HashMap<String, String>()
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.value
                //wth is is this
                Log.d("userListener -> onDataChange -> dataSnapshot.value", user.toString())

                if (user != null) {
                    concurrentUsersHashMap = user as HashMap<String, String>
                    Log.d("CURRENT USERS", concurrentUsersHashMap.toString())

                    concurrentUsersHashMap.forEach { it ->
                        adapter.data = adapter.data.plus(it.value)
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.e(
                    "userListener -> onCancelled",
                    databaseError.toException().printStackTrace().toString()
                )
                //do something here?
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("TAG GOES HERE","WOW! VI ÄR I FRAGMENTET ActiveUsersFragment!!!!!")
        root = inflater.inflate(R.layout.fragment_active_user,container,false)
        root.rootView.findViewById<RecyclerView>(R.id.active_user_recyclerView).adapter = adapter

        return root
    }
}