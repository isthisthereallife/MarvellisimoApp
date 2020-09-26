package isthisstuff.practice.marvellisimohdd.ui.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import io.realm.Realm
import isthisstuff.practice.marvellisimohdd.*
import isthisstuff.practice.marvellisimohdd.entities.MarvelObject
import isthisstuff.practice.marvellisimohdd.ui.details.DetailsActivity
import isthisstuff.practice.marvellisimohdd.ui.search.SearchFragment

class SearchResultsAdapter(private val fragment: SearchFragment) : RecyclerView.Adapter<MyViewHolder>() {

    private var offset: Int = 0
    private val realm: Realm = Realm.getDefaultInstance()

    var data: List<MarvelObject> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = data[position]

        val itemTitle: TextView = holder.view.findViewById(R.id.title_text)
        val itemThumbnail: ImageView = holder.view.findViewById(R.id.search_results_image)
        val itemStar:ImageView = holder.view.findViewById(R.id.search_favstar)

        if(checkIfSaveToCache()) saveSearchResultToCache(item)

        when(item.name) {
            null -> itemTitle.text = item.title
            else -> itemTitle.text = item.name
        }

        Picasso.get().load(item.thumbnail.path + "." + item.thumbnail.extension)
            .into(itemThumbnail)

        if(checkFavorite(item.id)) {
            itemStar.setImageResource(R.drawable.ic_baseline_star_filled_24)
        } else {
            itemStar.setImageResource(R.drawable.ic_baseline_star_border_24)
        }

        holder.view.findViewById<ConstraintLayout>(R.id.search_result_item)
            .setOnClickListener { openDetails(position) }
        Log.d("OFFSET",offset.toString())
        if (position == offset + 10) {
            offset += 20
            if(!fragment.onlyFavorites && isOnline(fragment.context)) fragment.runSearch(fragment.query, fragment.dataType, offset, false)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.search_item_view, parent, false) as ConstraintLayout
        return MyViewHolder(view)
    }

    private fun openDetails(position: Int) {
        val intent = Intent(fragment.context, DetailsActivity::class.java)
        intent.putExtra("item", this.data[position])
        fragment.startActivityForResult(intent, 1)
    }

    private fun checkIfSaveToCache(): Boolean {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.fragment.context)
        val saveToCacheBoolean = sharedPreferences.getBoolean("cache", false)
        return saveToCacheBoolean
    }

    private fun saveSearchResultToCache(marvelObject: MarvelObject) {
        val marvelRealmObject = convertMarvelObjectToMarvelRealmObject(marvelObject)
        realm.executeTransaction {
            realm.copyToRealmOrUpdate(marvelRealmObject)
        }
    }

    fun resetOffset(){
        offset = 0
    }
}


