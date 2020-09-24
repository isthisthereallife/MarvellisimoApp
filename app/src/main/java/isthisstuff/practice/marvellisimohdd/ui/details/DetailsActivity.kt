package isthisstuff.practice.marvellisimohdd.ui.details
import io.realm.Realm
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import android.widget.Button
import io.realm.kotlin.where
import android.content.Intent
import android.widget.TextView
import android.widget.ImageView
import androidx.activity.viewModels
import com.squareup.picasso.Picasso
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import isthisstuff.practice.marvellisimohdd.R
import androidx.appcompat.app.AppCompatActivity
import isthisstuff.practice.marvellisimohdd.database.User
import isthisstuff.practice.marvellisimohdd.checkFavorite
import isthisstuff.practice.marvellisimohdd.entities.MarvelObject
import isthisstuff.practice.marvellisimohdd.ui.data.MarvelViewModel
import isthisstuff.practice.marvellisimohdd.ui.activeusers.ActiveUsersActivity
import isthisstuff.practice.marvellisimohdd.convertMarvelObjectToMarvelRealmObject

class DetailsActivity : AppCompatActivity() {

    private lateinit var item: MarvelObject
    private lateinit var detailsName: TextView
    private lateinit var detailsText: TextView
    private lateinit var buttonShowMore: Button
    private lateinit var detailsMessage: Button
    private lateinit var detailsImage: ImageView
    private lateinit var detailsLinkMore: TextView
    private lateinit var detailsFavStar: ImageView
    private lateinit var detailsBackArrow: ImageView

    private val realm: Realm = Realm.getDefaultInstance()
    private var name: String = "Name goes here."
    private var description: String = "*NO DESCRIPTION AVAILABLE*"
    private var urlDetails: String = "https://Marvel.com"
    private var thumbnail: String = "https://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available.jpg"
    private var buttonText: String = ""
    private var favorite: Boolean = false
    private var activeUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private var dbUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        setSupportActionBar(findViewById(R.id.toolbar))
        item = (intent.getSerializableExtra("item") as MarvelObject)

        detailsName = findViewById<TextView>(R.id.details_name)
        detailsImage = findViewById<ImageView>(R.id.details_image)
        detailsText = findViewById<TextView>(R.id.details_text)
        detailsLinkMore = findViewById<TextView>(R.id.details_link_more)
        detailsFavStar = findViewById<ImageView>(R.id.details_favstar)
        detailsBackArrow = findViewById<ImageView>(R.id.details_arrow_back)
        buttonShowMore = findViewById<Button>(R.id.button_series)
        detailsMessage = findViewById<Button>(R.id.button_message)
        detailsFavStar.setOnClickListener { setFavorite() }
        detailsBackArrow.setOnClickListener { finish() }
        detailsMessage.setOnClickListener { sendToFriend(item) }

        updateDetailsInformation()

        if (checkFavorite(item.id)) {
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
                buttonText = "See all series ${item.name} appears in"
            buttonShowMore.text = buttonText
            buttonShowMore.setOnClickListener { showAllSeriesWithThisCharacter() }
            name = item.name.toString().replace("ï¿½", "'")
        } else if (item.title != null) {
            buttonText = "See all characters who appear in ${item.title}"
            buttonShowMore.text = buttonText
            buttonShowMore.setOnClickListener { showAllCharactersInThisSeries() }

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

    private fun showAllCharactersInThisSeries() {
        setContentView(R.layout.fragment_search)
        val m: MarvelViewModel by viewModels()
        m.getCharactersInSeries(item.id.toString(), 0)
        setContentView(R.layout.fragment_search)
    }

    private fun showAllSeriesWithThisCharacter() {
        setContentView(R.layout.fragment_search)
        val m: MarvelViewModel by viewModels()
        m.getSeriesContainingCharacter(item.id.toString(), 0)
        setContentView(R.layout.fragment_search)
    }

    fun sendToFriend(marvelObject: MarvelObject) {
        //setContentView(R.layout.fragment_active_user)
        if (activeUser != null) {

            intent = Intent(this, ActiveUsersActivity::class.java)
            intent.putExtra("MarvelObject", item)
            intent.putExtra("senderEmail", activeUser!!.email.toString())
            /*ActiveUsersAdapter().sender = activeUser!!.email.toString()
            ActiveUsersAdapter().marvelObjectId = item.id.toString()
             */
            startActivity(intent)

            /*


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