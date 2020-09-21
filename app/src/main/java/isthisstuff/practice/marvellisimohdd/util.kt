package isthisstuff.practice.marvellisimohdd

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

class MyViewHolder(val view: LinearLayout) : RecyclerView.ViewHolder(view) {

    val textView = view.findViewById<TextView>(R.id.title_text)
    val btnView = view.findViewById<ImageView>(R.id.search_results_image)
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

/*
// blame -> //M
fun convertFromMarvelRealmObjectToMarvelObject(marvelRealmObject: MarvelRealmObject): MarvelObject {
    val urls = mutableListOf<Urls>()
    marvelRealmObject.urls?.forEach {
        val urlsObject = Urls(type = it.type!!, url = it.url!!)
        urls.add(urlsObject)
    }
    val marvelObject = MarvelObject(
        id = marvelRealmObject.id!!, name = marvelRealmObject.name, title = marvelRealmObject.title,
        description = marvelRealmObject.description, thumbnail = Thumbnail(
            path = marvelRealmObject.thumbnail?.path!!,
            extension = marvelRealmObject.thumbnail?.extension!!
        ), urls = urls.toList()
    )
    return marvelObject
}
 */

/*

fun airplaneModeIsOn() : Boolean {
    //AIRPLANE-MODE check
    if (Settings.System.getInt(
            context?.contentResolver,
            Settings.Global.AIRPLANE_MODE_ON,
            0
        ) == 0
    ) {
        Log.d("Phone in airplane mode", "FALSE")
        return false
    } else {
        Log.d("Phone is in airplane mode", "TRUE")
        return true
    }
}
 */