package isthisstuff.practice.marvellisimohdd

import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyViewHolder(val view: LinearLayout) : RecyclerView.ViewHolder(view) {

    val textView = view.findViewById<TextView>(R.id.title_text)
    val btnView = view.findViewById<ImageView>(R.id.search_results_image)


}