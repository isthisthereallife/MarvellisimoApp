package isthisstuff.practice.marvellisimohdd.ui.details

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import isthisstuff.practice.marvellisimohdd.R
import isthisstuff.practice.marvellisimohdd.entities.MarvelObject

class DetailsActivity : AppCompatActivity() {

    private lateinit var star: ImageView
    private lateinit var item: MarvelObject
    private var thumbnail: String? =
        "https://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available.jpg"
    private var info: String? = "*NO DESCRIPTION AVAILABLE*"
    private var name: String? = "Name goes here."
    private var url_details: String? = "https://Marvel.com"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        setSupportActionBar(findViewById(R.id.toolbar))

        star = findViewById(R.id.details_favstar)
        star.setOnClickListener { changeStar() }

        /*
        val bundle:Bundle ?= intent.extras
        if(bundle!=null)
            thumbnail = bundle.getString("thumbnail")

         */

        item = (intent.getSerializableExtra("item") as MarvelObject)

        if(item.description!=null) {
            if (item.description.toString().isNotBlank())
                info = item.description.toString().replace("ï¿½", "'")
        }

        thumbnail = item.thumbnail.path + "." + item.thumbnail.extension

        if(item.name!=null) {
            if (item.name.toString().isNotBlank())
                name = item.name.toString().replace("ï¿½", "'")
        } else {
            name = item.title?.replace("ï¿½", "'")
        }

        if (item.urls[0].url.isNotBlank())
            url_details = item.urls[0].type + ": " + item.urls[0].url


        findViewById<TextView>(R.id.details_name).text = name
        findViewById<TextView>(R.id.text_details).text = info
        findViewById<TextView>(R.id.details_text_view_urls).text = url_details
        Picasso.get().load(thumbnail).into(findViewById<ImageView>(R.id.imageView2))
    }

    private fun changeStar() {
        if (star != null) {
            when (star.tag) {
                "Filled" -> {
                    star.setImageResource(R.drawable.ic_baseline_star_border_24)
                    star.setTag("Bordered")
                    Toast.makeText(this, "Removed from favourites", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    star.setImageResource(R.drawable.ic_baseline_star_filled_24)
                    star.setTag("Filled")
                    Toast.makeText(this, "Added to favourites", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}