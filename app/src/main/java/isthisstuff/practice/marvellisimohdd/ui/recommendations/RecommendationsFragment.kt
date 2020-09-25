package isthisstuff.practice.marvellisimohdd.ui.recommendations

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
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
import isthisstuff.practice.marvellisimohdd.entities.MarvelObject
import isthisstuff.practice.marvellisimohdd.entities.Thumbnail
import isthisstuff.practice.marvellisimohdd.entities.Urls
import isthisstuff.practice.marvellisimohdd.ui.adapter.SearchResultsAdapter
import isthisstuff.practice.marvellisimohdd.ui.data.MarvelViewModel
import isthisstuff.practice.marvellisimohdd.ui.details.DetailsActivity

class RecommendationsFragment : Fragment() {

    private var database = FirebaseDatabase.getInstance()
    private val mAuth = FirebaseAuth.getInstance()
    private val currentUser = mAuth.currentUser?.email
    private val currentUserNoDots = currentUser.toString().replace(".", ",")
    private lateinit var target: LinearLayout
    private var activeUserMessagesReference = database.getReference("<TO:$currentUserNoDots>")
    private var detachedView = false

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
        target = root.findViewById(R.id.put_reco_items_here)
        if (currentUser != null) {

            //FIREBASE database
            val messageListener = object : ValueEventListener {
                var concurrentMessagesHashMap = HashMap<String, String>()

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (detachedView) {
                        Log.d("isDetached == true", "DETATCHING VALUE EVENT LISTENER")
                        activeUserMessagesReference.removeEventListener(this)
                        return
                    }
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
            Log.d("Inflating fragment", "ATTACHING VALUE EVENT LISTENER")
            activeUserMessagesReference.addValueEventListener(messageListener)
        } else Toast.makeText(this.context, "Log in to use this cool feature", Toast.LENGTH_SHORT)
            .show()
        return root
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("onDetach", "Detaching...")
        detachedView = true

        //    private var activeUserMessagesReference = database.getReference("<TO:$currentUserNoDots>")
    }


    fun printMessages(toList: List<Pair<String, String>>) {
        toList.forEach { it ->
            val textView = TextView(context)
            textView.textSize = 15f
            val sender = it.second.substringAfter("<SENDER>").substringBefore("</SENDER>")
            val type = it.second.substringAfter("<MARVELTYPE>").substringBefore("</MARVELTYPE>")
            val name = it.second.substringAfter("<MARVELOBJECTNAME>")
                .substringBefore("</MARVELOBJECTNAME>")
            val payload = it.second.substringAfter("<PAYLOAD>").substringBefore("</PAYLOAD")

            val text = "$sender thinks you should search for the $type $name \n"
            Log.d("printMessages", "SKRIVER UT EN TEXTVY")
            textView.text = text
            target.addView(textView)
            //pallar inte göra MarvelObject parcelable. detta är en lösning. deal with it.
            val marvelObject = makeMarvelObjectFromPayload(payload, type)
            val intent = Intent(this.context, DetailsActivity::class.java)
            intent.putExtra("item", marvelObject)
            textView.setOnClickListener { startActivity(intent) }
        }

        /* //GAMMLA LÖSNINGEN
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
        */
    }

    fun makeMarvelObjectFromPayload(marvelString: String, type: String): MarvelObject {
        val id = marvelString.substringAfter("MarvelObject(id=").substringBefore(", name=")
        val name = if (type.equals("character")) marvelString.substringAfter(", name=")
            .substringBefore(", title=") else null
        val title = if (type.equals("series")) marvelString.substringAfter(", title=")
            .substringBefore(", urls=") else null
        val urlType = marvelString.substringAfter("urls=[Urls(type=").substringBefore(", url=")
        val url = marvelString.substringAfter(", url=").substringBefore(",")
        Log.d("fan osäker på denna url asså", url)
        val urls = Urls(type = urlType, url = url)
        val description = marvelString.substringAfter(", description=").substringBefore(",")
        val path = marvelString.substringAfter(", thumbnail=Thumbnail(path=")
            .substringBefore(", extension=")
        val extension = marvelString.substringAfter(", extension=").substringBefore(")")
        val thumbnail = Thumbnail(path = path, extension = extension)


        Log.d("MARVELSTRING", marvelString)
        val marvelObject =
            MarvelObject(id.toInt(), name, title, listOf(urls), description, thumbnail)
        Log.d("marvelObject.toString()", marvelObject.toString())
        return marvelObject
    }
}