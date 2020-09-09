package isthisstuff.practice.marvellisimohdd.ui.details

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import isthisstuff.practice.marvellisimohdd.R

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        setSupportActionBar(findViewById(R.id.toolbar))


        val star : ImageView = findViewById(R.id.details_favstar)
        star.setOnClickListener({changeStar(star)})
    }

    private fun changeStar(star: ImageView?) {
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