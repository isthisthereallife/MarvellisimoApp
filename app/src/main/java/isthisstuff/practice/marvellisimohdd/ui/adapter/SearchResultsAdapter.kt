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
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import io.realm.Realm
import io.realm.kotlin.where
import isthisstuff.practice.marvellisimohdd.R
import isthisstuff.practice.marvellisimohdd.MyViewHolder
import isthisstuff.practice.marvellisimohdd.convertMarvelObjectToMarvelRealmObject
import isthisstuff.practice.marvellisimohdd.database.MarvelRealmObject
import isthisstuff.practice.marvellisimohdd.database.SearchQueryRealmObject
import isthisstuff.practice.marvellisimohdd.database.UrlsRealmObject
import isthisstuff.practice.marvellisimohdd.entities.MarvelObject
import isthisstuff.practice.marvellisimohdd.ui.data.MarvelDatatypes
import isthisstuff.practice.marvellisimohdd.ui.data.MarvelViewModel
import isthisstuff.practice.marvellisimohdd.ui.details.DetailsActivity

class SearchResultsAdapter(private val fragment: Fragment) : RecyclerView.Adapter<MyViewHolder>() {

    lateinit var itemNameView: TextView
    lateinit var itemInfoView: TextView
    lateinit var itemThumbnail: ImageView
    private lateinit var realm: Realm

    var data: List<MarvelObject> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    lateinit var marvelViewModel: MarvelViewModel
    lateinit var marvelDatatype: MarvelDatatypes
    lateinit var preferredSearchMethod: String
    lateinit var query: String
    var offset: Int = 0

    init {
        //TODO lägg findViewById-grejjerna häri? /M
    }

    fun saveRequestData(
        marvelViewModel: MarvelViewModel,
        marvelDatatype: MarvelDatatypes,
        query: String,
        offset: Int,
        preferredSearchMethod: String
    ) {
        this.marvelViewModel = marvelViewModel
        this.marvelDatatype = marvelDatatype
        this.query = query
        this.offset = offset
        this.preferredSearchMethod = preferredSearchMethod
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = data[position]

        //spara till cache kanske? kollar och gör /M
        //men tänk om detta redan kommer från cachen?? hmmmm /M

        saveSearchResultToCache(item, checkIfSaveToCache())

        if (item.name != null) {
            holder.view.findViewById<TextView>(R.id.title_text).text = item.name
        } else {
            holder.view.findViewById<TextView>(R.id.title_text).text = item.title
        }

        holder.view.findViewById<TextView>(R.id.info_text).text = item.description
        Picasso.get().load(item.thumbnail.path + "." + item.thumbnail.extension)
            .into(holder.view.findViewById<ImageView>(R.id.search_results_image))
        holder.view.findViewById<LinearLayout>(R.id.search_result_item)
            .setOnClickListener { openDetails(it, position) }

        if (position == offset + 10) {

            offset += 20
            Log.d("RecyclerView", "Reached the end. Position: $position \t Offset: $offset")
            marvelViewModel.getData(marvelDatatype, query, offset, preferredSearchMethod)
        }
    }

    private fun openDetails(view: View, position: Int) {
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
        val view = layoutInflater.inflate(R.layout.search_item_view, parent, false) as LinearLayout
        realm = Realm.getDefaultInstance()
        saveQueryToCache(query, checkIfSaveToCache())

        return MyViewHolder(view)
    }

    private fun checkIfSaveToCache(): Boolean {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.fragment.context)
        val saveToCacheBoolean = sharedPreferences.getBoolean("cache", false)
        Log.d("Preferensen som heter cache har detta värde", saveToCacheBoolean.toString())
        return saveToCacheBoolean
    }

    private fun saveQueryToCache(query: String, cachingActivated: Boolean = false) {
        if (cachingActivated) {
            val searchQuery = SearchQueryRealmObject()

            searchQuery.query = query
            realm.beginTransaction()
            realm.copyToRealmOrUpdate(searchQuery)
            realm.commitTransaction()
            Log.d("Search query saved to cache", "Query: ${searchQuery.query}")
            Log.d(
                "Queries currently in cache",
                realm.where<SearchQueryRealmObject>().findAll().toString()
            )
        }
    }

    private fun saveSearchResultToCache(marvelObject: MarvelObject, cachingActivated: Boolean) {
        if (cachingActivated) {
            //borde vi spara ner en kopia av bilderna också? /M
            val marvelRealmObject = convertMarvelObjectToMarvelRealmObject(marvelObject)
            realm.beginTransaction()
            realm.copyToRealmOrUpdate(marvelRealmObject)
            realm.commitTransaction()
            Log.d(
                "ONE OBJECT ?SAVED? (if not saved before!)",
                "OBJECT NAME: ${marvelRealmObject.name}"
            )
            Log.d(
                "NUMBER OF CACHED MARVELOBJOKTS",
                realm.where<MarvelRealmObject>().findAll().size.toString()
            )
        }
    }
}


