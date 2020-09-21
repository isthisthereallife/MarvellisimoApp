package isthisstuff.practice.marvellisimohdd.ui.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import io.realm.Realm
import io.realm.kotlin.where
import isthisstuff.practice.marvellisimohdd.R
import isthisstuff.practice.marvellisimohdd.database.MarvelRealmObject
import isthisstuff.practice.marvellisimohdd.database.User
import isthisstuff.practice.marvellisimohdd.entities.MarvelObject
import isthisstuff.practice.marvellisimohdd.ui.series.SeriesFragment

class DetailsActivity : AppCompatActivity() {

    private lateinit var star: ImageView
    private lateinit var item: MarvelObject
    private var thumbnail: String? =
        "https://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available.jpg"
    private var info: String? = "*NO DESCRIPTION AVAILABLE*"
    private var name: String? = "Name goes here."
    private lateinit var series: List<MarvelObject>
    private var urlDetails: String? = "https://Marvel.com"
    private lateinit var realm: Realm
    private lateinit var activeUser: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        setSupportActionBar(findViewById(R.id.toolbar))

        //REALM
        realm = Realm.getDefaultInstance()

        //TODO borde vi lägga alla findViewById här uppe? //mvhmagnus
        val urlTextView: TextView = findViewById<TextView>(R.id.details_text_view_url)


        //hämta in user1 från databasen
        activeUser = realm.where<User>().findFirst()!!

        star = findViewById(R.id.details_favstar)
        star.setOnClickListener { changeStar() }


        //TODO klipp ut till funktioner


        item = (intent.getSerializableExtra("item") as MarvelObject)

        if (item.description != null) {
            if (item.description.toString().isNotBlank())
                info = item.description.toString()
                    .replace("ï¿½", "'")
                    .replace("â€™", "'")
        }

        thumbnail = item.thumbnail.path + "." + item.thumbnail.extension

        if (item.name != null) {
            if (item.name.toString().isNotBlank())
                name = item.name.toString().replace("ï¿½", "'")
        } else {
            name = item.title?.replace("ï¿½", "'")
        }
        if (item.urls[0].url.isNotBlank()) {
            urlDetails = item.urls[0].type
            urlTextView.setOnClickListener {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(item.urls[0].url)
                    )
                )
            }
            urlTextView.text = urlDetails
        }

        findViewById<ImageView>(R.id.details_arrow_back).setOnClickListener { finish() }
        findViewById<TextView>(R.id.details_name).text = name
        findViewById<TextView>(R.id.text_details).text = info
        Picasso.get().load(thumbnail).into(findViewById<ImageView>(R.id.imageView2))

        val button = findViewById<Button>(R.id.button_series)
        button.setOnClickListener{
            Toast.makeText(this.applicationContext, "Redirect", Toast.LENGTH_SHORT).show()
        }
    }

    private fun changeStar() {
        if (star != null) {
            when (star.tag) {
                "Filled" -> {
                    star.setImageResource(R.drawable.ic_baseline_star_border_24)
                    star.setTag("Bordered")
                    Toast.makeText(this, "Removed $name from favourites", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    //spara ner
                    Log.d("ACTIVE USER INNAN: ", activeUser.toString())

                    realm.beginTransaction()
                    var newObject = MarvelRealmObject()
                    newObject.name = item.name //se upp!!!!! series Title
                    newObject.id = item.id
                    activeUser?.favorites?.add(newObject)
                    val a = activeUser!!
                    realm.copyToRealmOrUpdate(a)
                    realm.commitTransaction()

                    Log.d("EFTER LAGT TILL", "hämtat alla: " + realm.where<User>().findAll())

                    Log.d("ACTIVE USER EFTER: ", activeUser.toString())



                    star.setImageResource(R.drawable.ic_baseline_star_filled_24)
                    star.setTag("Filled")
                    Toast.makeText(this, "Added $name to favourites", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}