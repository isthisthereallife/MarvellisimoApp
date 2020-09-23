package isthisstuff.practice.marvellisimohdd.ui.details

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import io.realm.Realm
import io.realm.RealmObject
import io.realm.kotlin.where
import isthisstuff.practice.marvellisimohdd.R
import isthisstuff.practice.marvellisimohdd.checkFavorite
import isthisstuff.practice.marvellisimohdd.ui.activeusers.ActiveUsersActivity
import isthisstuff.practice.marvellisimohdd.convertMarvelObjectToMarvelRealmObject
import isthisstuff.practice.marvellisimohdd.database.MarvelRealmObject
import isthisstuff.practice.marvellisimohdd.database.User
import isthisstuff.practice.marvellisimohdd.entities.MarvelObject
import isthisstuff.practice.marvellisimohdd.ui.data.MarvelDatatypes
import isthisstuff.practice.marvellisimohdd.ui.data.MarvelViewModel
import isthisstuff.practice.marvellisimohdd.ui.search.SearchFragment
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DetailsActivity : AppCompatActivity() {

    private val realm: Realm = Realm.getDefaultInstance()
    private lateinit var item: MarvelObject


    private lateinit var detailsName:TextView
    private lateinit var detailsImage:ImageView
    private lateinit var detailsText:TextView
    private lateinit var detailsLinkMore:TextView
    private lateinit var detailsFavStar:ImageView
    private lateinit var detailsBackArrow:ImageView
    private lateinit var detailsSeries:Button
    private lateinit var detailsMessage:Button

    private var name: String = "Name goes here."
    private var description: String = "*NO DESCRIPTION AVAILABLE*"
    private var urlDetails: String = "https://Marvel.com"
    private var thumbnail: String =
        "https://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available.jpg"

    private var favorite: Boolean = false

    private var activeUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private var dbUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        setSupportActionBar(findViewById(R.id.toolbar))

        detailsName = findViewById<TextView>(R.id.details_name)
        detailsImage = findViewById<ImageView>(R.id.details_image)
        detailsText = findViewById<TextView>(R.id.details_text)
        detailsLinkMore = findViewById<TextView>(R.id.details_link_more)
        detailsFavStar = findViewById<ImageView>(R.id.details_favstar)
        detailsBackArrow = findViewById<ImageView>(R.id.details_arrow_back)
        detailsSeries = findViewById<Button>(R.id.button_series)
        detailsMessage = findViewById<Button>(R.id.button_message)


        item = (intent.getSerializableExtra("item") as MarvelObject)

        updateDetailsInformation()

        detailsFavStar.setOnClickListener { setFavorite() }
        detailsBackArrow.setOnClickListener { finish() }
        detailsSeries.setOnClickListener { showCharacterSeries() }
        detailsMessage.setOnClickListener { sendToFriend(item) }

        if(checkFavorite(item.id)) {
            favorite = true
            detailsFavStar.setImageResource(R.drawable.ic_baseline_star_filled_24)
        }
    }

    private fun setFavorite() {
        if (activeUser != null) {
            dbUser = realm.where<User>().equalTo("email", activeUser!!.email).findFirst()
            if (favorite) {
                favorite = false

                for (x in dbUser!!.favorites) {
                    if (x.id == item.id) {
                        realm.executeTransaction {
                            dbUser!!.favorites.remove(x)
                            realm.copyToRealmOrUpdate(dbUser)
                        }
                        println(
                            realm.where<User>().equalTo("email", activeUser!!.email).findFirst()
                        )
                        break
                    }
                }

                detailsFavStar.setImageResource(R.drawable.ic_baseline_star_border_24)
                Toast.makeText(this, "Removed $name from favorites", Toast.LENGTH_SHORT).show()

            } else {
                favorite = true

                realm.executeTransaction {
                    val newFavorite = convertMarvelObjectToMarvelRealmObject(item)
                    dbUser!!.favorites.add(newFavorite)
                    realm.copyToRealmOrUpdate(dbUser)
                }
                println(realm.where<User>().equalTo("email", activeUser!!.email).findFirst())

                detailsFavStar.setImageResource(R.drawable.ic_baseline_star_filled_24)
                Toast.makeText(this, "Added $name to favorites", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Try logging in first!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateDetailsInformation() {
        if (item.name != null) {
            if (item.name.toString().isNotBlank())
                name = item.name.toString().replace("ï¿½", "'")
        } else if (item.title != null) {
            name = item.title.toString().replace("ï¿½", "'")
        }

        if (item.description != null) {
            if (item.description.toString().isNotBlank())
                description = item.description.toString()
                    .replace("ï¿½", "'")
                    .replace("â€™", "'")
        }

        if (item.urls[0].url.isNotBlank()) {
            detailsLinkMore.text = item.urls[0].type
            detailsLinkMore.setOnClickListener {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(item.urls[0].url)
                    )
                )
            }
        }

        detailsName.text = name
        detailsText.text = description
        detailsLinkMore.text = urlDetails
        thumbnail = item.thumbnail.path + "." + item.thumbnail.extension
        Picasso.get().load(thumbnail).into(detailsImage)
    }


    private fun showCharacterSeries(){
        Toast.makeText(this.applicationContext, item.name.toString(), Toast.LENGTH_SHORT).show()

        setContentView(R.layout.fragment_search)
        /*val fragment:SearchFragment = supportFragmentManager.findFragmentById(R.id.fragment_search) as SearchFragment
          fragment.performSearch(query = item.name.toString(), dataType = MarvelDatatypes.SERIES)*/

        val m : MarvelViewModel by viewModels()
        m.getData(MarvelDatatypes.CHARACTERS,item.id.toString(),0,"strict")

        Toast.makeText(this.applicationContext, "yay", Toast.LENGTH_SHORT).show()

    }




    fun sendToFriend(marvelObject: MarvelObject) {
        //setContentView(R.layout.fragment_active_user)
        startActivity(Intent(this, ActiveUsersActivity::class.java))
        if (activeUser != null) {
            /*
            // val listOfUsers = getActiveUsers()
            val database = FirebaseDatabase.getInstance()


            //ändra message till "vem som skickat och till vem det ska" (och en timestamp)
            //jag är denna -> getString(R.string.msg_token_fmt,token)
            //jag är denna -> activeUser.email
            val timestamp = LocalDateTime.now()
            val timestring = timestamp.toString().replace(".", ":")


            Log.d("CURRENT TIME FORMATTED", "timestamp: $timestring")
            var sender = activeUser?.email.toString()
            val target = "put@target.here"

            val message =
                "SENDER<${removeDotFromEmail(sender)}>RECEIVER<${removeDotFromEmail(target)}>TIME<$timestring>"
            Log.d("MESSAGE", message)
            val myReference = database.getReference(message)



            if (item.name != null)
                myReference.setValue("Hello world, i, ${activeUser?.displayName} am sending the name of ${item.name}")
            else myReference.push()
                .setValue("Hello world, i, ${activeUser?.displayName} am sending the name of ${item.title}")
            */
        } else {
            Toast.makeText(this, "Try logging in first!", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     *
     * Hämta lista på användare, skicka tillbaka listan
     */
    //  fun getActiveUsers() : List<String>{
    //val c = concurrentUsersHashMap
//    }

    fun removeDotFromEmail(email: String?): String? {
        return email?.replace(".", ",")
    }

}