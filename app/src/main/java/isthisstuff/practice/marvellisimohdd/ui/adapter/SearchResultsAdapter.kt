package isthisstuff.practice.marvellisimohdd.ui.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import isthisstuff.practice.marvellisimohdd.R
import isthisstuff.practice.marvellisimohdd.MyViewHolder
import isthisstuff.practice.marvellisimohdd.entities.MarvelObject
import isthisstuff.practice.marvellisimohdd.ui.characters.CharactersFragment
import isthisstuff.practice.marvellisimohdd.ui.details.DetailsActivity

class SearchResultsAdapter(private val fragment: Fragment) : RecyclerView.Adapter<MyViewHolder>() {
    lateinit var itemNameView: TextView
    lateinit var itemInfoView: TextView
    lateinit var itemThumbnail: ImageView

    var data: List<MarvelObject> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    init {
        //TODO lägg findViewById-grejjerna häri
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var item = data[position]

        if(item.name!=null) {
            holder.view.findViewById<TextView>(R.id.title_text).text = item.name
        } else {
            holder.view.findViewById<TextView>(R.id.title_text).text = item.title
        }

        holder.view.findViewById<TextView>(R.id.info_text).text = item.description
        Picasso.get().load(item.thumbnail.path + "." + item.thumbnail.extension)
            .into(holder.view.findViewById<ImageView>(R.id.search_results_image))
        holder.view.findViewById<LinearLayout>(R.id.search_result_item)
            .setOnClickListener { openDetails(it, position) }

        if(position==19) {
            println("NU HAR VI NÅTT SLUTET, SÄG ADJÖ TILL NÄRA OCH KÄRA")

        }
    }

    fun openDetails(view: View, position: Int) {
        Log.d(
            "searchResultClicked",
            "klickade på view: $view \n och this: $this \n och this.data:${this.data[position]}"
        )
        val intent = Intent(fragment.context, DetailsActivity::class.java)
        intent.putExtra("item", this.data[position])
        fragment.context?.startActivity(intent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        var view = layoutInflater.inflate(R.layout.search_item_view, parent, false) as LinearLayout
        return MyViewHolder(view)
    }
/*
    fun picassoImg(thumbnail: Thumbnail): Uri? {
        return Picasso.get().load(thumbnail.path+thumbnail.extension).resize(50,50)
            .centerCrop()
            .into(R.id.search_results_image)
            .setIndicatorsEnabled(true)
    }
*/
}


