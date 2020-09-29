package isthisstuff.practice.marvellisimohdd.ui.recommendations

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import isthisstuff.practice.marvellisimohdd.R
import isthisstuff.practice.marvellisimohdd.entities.MarvelObject
import isthisstuff.practice.marvellisimohdd.entities.Thumbnail
import isthisstuff.practice.marvellisimohdd.entities.Urls
import isthisstuff.practice.marvellisimohdd.ui.data.MarvelDatatypes
import isthisstuff.practice.marvellisimohdd.ui.details.DetailsActivity

class RecommendationsFragment : Fragment() {

    private var database = FirebaseDatabase.getInstance()
    private val mAuth = FirebaseAuth.getInstance()
    private val currentUser = mAuth.currentUser?.email
    private val currentUserNoDots = currentUser.toString().replace(".", ",")
    private lateinit var target: LinearLayout
    private var activeUserMessagesReference = database.getReference("<TO:$currentUserNoDots>")
    private var detachedView = false

    lateinit var root: View

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
        } else Toast.makeText(
            this.context,
            getText(R.string.toast_log_in_prompt),
            Toast.LENGTH_SHORT
        )
            .show()
        return root
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("onDetach", "Detaching...")
        detachedView = true

    }


    fun printMessages(toList: List<Pair<String, String>>) {
        target.removeAllViews()
        toList.forEach { it ->
            val view = inflate(this.context, R.layout.search_item_view, null)
            val msgReference = it.first
            val sender = "Recommended by " + it.second.substringAfter("<SENDER>")
                .substringBefore("</SENDER>")
            val type = it.second.substringAfter("<MARVELTYPE>").substringBefore("</MARVELTYPE>")
            val name = it.second.substringAfter("<MARVELOBJECTNAME>")
                .substringBefore("</MARVELOBJECTNAME>")
            val payload = it.second.substringAfter("<PAYLOAD>").substringBefore("</PAYLOAD")
            val marvelObject = makeMarvelObjectFromPayload(payload, type)
            val img = view.findViewById<ImageView>(R.id.search_results_image)
            Picasso.get().load(marvelObject.thumbnail.path + "." + marvelObject.thumbnail.extension)
                .into(img)
            val innerConstraint =
                view.findViewById<ConstraintLayout>(R.id.search_result_inner_constraint)
            innerConstraint.findViewById<TextView>(R.id.title_text).text = name
            innerConstraint.findViewById<TextView>(R.id.info_text).text = sender
            val xImage = innerConstraint.findViewById<ImageView>(R.id.search_favstar)
            xImage.setImageResource(R.drawable.ic_baseline_x_24)
            target.addView(view)
            val intent = Intent(this.context, DetailsActivity::class.java)
            intent.putExtra("item", marvelObject)
            view.setOnClickListener { startActivityForResult(intent, 2) }
            xImage.setOnClickListener {
                activeUserMessagesReference.child(msgReference).removeValue()
                target.removeView(view)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            1 -> {
                val relatedData: Bundle = bundleOf(
                    "query" to data!!.extras!!.get("query"),
                    "dataType" to data.extras!!.get("dataType")
                )

                when (data.extras!!.get("dataType")) {
                    MarvelDatatypes.CHARACTERS -> findNavController().navigate(
                        R.id.nav_character,
                        relatedData
                    )
                    MarvelDatatypes.SERIES -> findNavController().navigate(
                        R.id.nav_series,
                        relatedData
                    )
                }
            }
        }
    }

    private fun makeMarvelObjectFromPayload(marvelString: String, type: String): MarvelObject {
        val id = marvelString.substringAfter("MarvelObject(id=").substringBefore(", name=")
        val name = if (type.equals("character")) marvelString.substringAfter(", name=")
            .substringBefore(", title=") else null
        val title = if (type.equals("series")) marvelString.substringAfter(", title=")
            .substringBefore(", urls=") else null
        val urlType = marvelString.substringAfter("urls=[Urls(type=").substringBefore(", url=")
        val url = marvelString.substringAfter(", url=").substringBefore(",")
        val urls = Urls(type = urlType, url = url)
        val description = marvelString.substringAfter(", description=").substringBefore(",")
        val path = marvelString.substringAfter(", thumbnail=Thumbnail(path=")
            .substringBefore(", extension=")
        val extension = marvelString.substringAfter(", extension=").substringBefore(")")
        val thumbnail = Thumbnail(path = path, extension = extension)

        return MarvelObject(id.toInt(), name, title, listOf(urls), description, thumbnail)
    }
}