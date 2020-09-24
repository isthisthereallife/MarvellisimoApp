package isthisstuff.practice.marvellisimohdd.ui.recommendations

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.realm.Realm
import io.realm.kotlin.where
import isthisstuff.practice.marvellisimohdd.R
import isthisstuff.practice.marvellisimohdd.database.User
import isthisstuff.practice.marvellisimohdd.ui.adapter.SearchResultsAdapter
import isthisstuff.practice.marvellisimohdd.ui.data.MarvelViewModel

class RecommendationsFragment : Fragment() {

    private var database = FirebaseDatabase.getInstance()
    private val mAuth = FirebaseAuth.getInstance()
    private val currentUser = mAuth.currentUser?.email
    private val currentUserNoDots = currentUser.toString().replace(".", ",")

    private var activeUserMessagesReference = database.getReference("<TO:$currentUserNoDots>")

    //private var activeUserMessagesReference = database.getReference("currentUsers")
    lateinit var root: View


    val marvelViewModel: MarvelViewModel by viewModels()
    private var messagesList = listOf<Pair<String, String>>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_recommendations, container, false)
        if (currentUser != null) {

            //FIREBASE database
            val messageListener = object : ValueEventListener {
                var concurrentMessagesHashMap = HashMap<String, String>()

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val messages = dataSnapshot.value
                    if (messages != null) {
                        concurrentMessagesHashMap = messages as HashMap<String, String>
                        printMessages(concurrentMessagesHashMap.toList())
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
            activeUserMessagesReference.addValueEventListener(messageListener)
        } else Toast.makeText(this.context, "Log in to use this cool feature", Toast.LENGTH_SHORT)
            .show()
        return root
    }


    fun printMessages(toList: List<Pair<String, String>>) {
        val recoTextView = root.findViewById<TextView>(R.id.recommendations_text_view)
        var textBuilder = ""
        toList.forEach { it ->
            Log.d("Loopar meddelanden att skriva ut", it.second)
            val sender = it.second.substringAfter("<SENDER>").substringBefore("</SENDER>")
            val type = it.second.substringAfter("<MARVELTYPE>").substringBefore("</MARVELTYPE>")
            val name = it.second.substringAfter("<MARVELOBJECTNAME>").substringBefore("</MARVELOBJECTNAME>")

            textBuilder = textBuilder.plus("$sender thinks you should search for the $type $name").plus("\n\n")
        }
        recoTextView.text = textBuilder
    }
}