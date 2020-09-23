package isthisstuff.practice.marvellisimohdd.ui.activeusers

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import isthisstuff.practice.marvellisimohdd.R
import isthisstuff.practice.marvellisimohdd.ui.adapter.ActiveUsersAdapter

class ActiveUsersFragment : Fragment() {
    lateinit var root: View
    private var database = FirebaseDatabase.getInstance()
    private var databaseCurrentUsersReference = database.getReference("currentUsers")
    var concurrentUsersHashMap = HashMap<String, String>()
    private var adapter: ActiveUsersAdapter = ActiveUsersAdapter()

    init {
        //FIREBASE database
        val userListener = object : ValueEventListener {
            var concurrentUsersHashMap = HashMap<String, String>()

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.value

                //detta skriver ut alla users från currentUsers i Firebase Realtime Database
                Log.d("ActiveUsersFragment -> userListener -> onDataChange -> dataSnapshot.value", user.toString())

                if (user != null) {
                    concurrentUsersHashMap = user as HashMap<String, String>
                    Log.d("CURRENT USERS", concurrentUsersHashMap.toString())
                    //DETTA HÄR OVANFÖR FUNKAR!
                    adapter.data = concurrentUsersHashMap
                    Log.d("ActiveUsersFragment -> userListener -> onDataChange -> adapter.data","HAHAHAHAH")

/*
                    //TODO
                    //DETTA HÄR UNDER ÄR FELET
                    concurrentUsersHashMap.values.forEach {
                        Log.d("Value to save to adapter.data",it)
                        adapter.data = adapter.data.plus(it)

                        //TODO denna ändrar inte värdet på data i ActiveUsersAdapter (eftersom jag inte når denna)
                        Log.d("VÄRDET PÅ ADAPTERNS DATA",adapter.data.toString())
                        adapter.notifyItemInserted(adapter.data.size-1)
                    }

 */
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
        databaseCurrentUsersReference.addValueEventListener(userListener)

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