package isthisstuff.practice.marvellisimohdd.ui.details

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import isthisstuff.practice.marvellisimohdd.R
import isthisstuff.practice.marvellisimohdd.entities.MarvelObject

class DetailsActivity : AppCompatActivity() {

    private lateinit var star : ImageView
    private lateinit var item:MarvelObject
    private var thumbnail:String? = "https://i.annihil.us/u/prod/marvel/i/mg/b/40/image_not_available.jpg"
    private var info:String? = "There was a description here, it is gone now"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        setSupportActionBar(findViewById(R.id.toolbar))

        star = findViewById(R.id.details_favstar)
        star.setOnClickListener{ changeStar() }

        /*
        val bundle:Bundle ?= intent.extras
        if(bundle!=null)
            thumbnail = bundle.getString("thumbnail")

         */

        item = (intent.getSerializableExtra("item") as MarvelObject)

        if(item.description.isNotBlank())
        info = item.description.replace("ï¿½","'")
        thumbnail = item.thumbnail.path+"."+item.thumbnail.extension

        findViewById<TextView>(R.id.text_details).text = info
        Picasso.get().load(thumbnail).into(findViewById<ImageView>(R.id.imageView2))
    }

    private fun changeStar() {
        if (star != null) {
            when (star.tag) {
                "Filled" -> {
                    star.setImageResource(R.drawable.ic_baseline_star_border_24)
                    star.setTag("Bordered")
                    Toast.makeText(this,"Removed from favourites",Toast.LENGTH_SHORT).show()
                }
                else -> {
                    star.setImageResource(R.drawable.ic_baseline_star_filled_24)
                    star.setTag("Filled")
                    Toast.makeText(this,"Added to favourites",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}