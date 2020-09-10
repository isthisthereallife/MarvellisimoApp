package isthisstuff.practice.marvellisimohdd.ui.details

import android.os.Bundle
import android.widget.ImageView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import isthisstuff.practice.marvellisimohdd.R

class DetailsActivity : AppCompatActivity() {

    lateinit var star : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        setSupportActionBar(findViewById(R.id.toolbar))

        star = findViewById(R.id.details_favstar)
        star.setOnClickListener{ changeStar() }
    }

    private fun changeStar() {
        if (star != null) {
            when (star.tag) {
                "Filled" -> {
                    star.setImageResource(R.drawable.ic_baseline_star_border_24)
                    star.setTag("Bordered")
                }
                else -> {
                    star.setImageResource(R.drawable.ic_baseline_star_filled_24)
                    star.setTag("Filled")
                }
            }
        }
    }
}