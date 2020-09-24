package isthisstuff.practice.marvellisimohdd

import android.app.Activity
import android.content.Context
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import io.realm.Realm
import io.realm.kotlin.where
import isthisstuff.practice.marvellisimohdd.database.MarvelRealmObject
import isthisstuff.practice.marvellisimohdd.database.ThumbnailRealmObject
import isthisstuff.practice.marvellisimohdd.database.UrlsRealmObject
import isthisstuff.practice.marvellisimohdd.database.User
import isthisstuff.practice.marvellisimohdd.entities.MarvelObject
import isthisstuff.practice.marvellisimohdd.entities.Thumbnail
import isthisstuff.practice.marvellisimohdd.entities.Urls

class MyViewHolder(val view: ConstraintLayout) : RecyclerView.ViewHolder(view) {

    val textView = view.findViewById<TextView>(R.id.title_text)
    val btnView = view.findViewById<ImageView>(R.id.search_results_image)
}

class MyActiveUsersViewHolder(val view: LinearLayout) : RecyclerView.ViewHolder(view) {
    val userEmail = view.findViewById<TextView>(R.id.user_email_text_view)
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

fun convertMarvelObjectToMarvelRealmObject(marvelObject: MarvelObject): MarvelRealmObject {

    val marvelRealmObject = MarvelRealmObject()
    marvelRealmObject.id = marvelObject.id
    marvelRealmObject.name = marvelObject.name
    marvelRealmObject.title = marvelObject.title
    marvelRealmObject.description = marvelObject.description
    marvelRealmObject.thumbnail = ThumbnailRealmObject()
    marvelRealmObject.thumbnail!!.path = marvelObject.thumbnail.path
    marvelRealmObject.thumbnail!!.extension = marvelObject.thumbnail.extension
    marvelObject.urls.forEach {
        val urlsRealmObject = UrlsRealmObject()
        urlsRealmObject.type = it.type
        urlsRealmObject.url = it.url
        marvelRealmObject.urls?.add(urlsRealmObject)
    }
    return marvelRealmObject
}

// blame -> //M
fun convertMarvelRealmObjectToMarvelObject(marvelRealmObject: MarvelRealmObject): MarvelObject {

    var urls = listOf<Urls>()
    marvelRealmObject.urls?.forEach {
        val urlsObject = Urls(type = it.type!!, url = it.url!!)
        urls = urls.plus(urlsObject)
    }

    var marvelObject = MarvelObject(
        marvelRealmObject.id,
        marvelRealmObject.name,
        marvelRealmObject.title,
        urls,
        marvelRealmObject.description,
        Thumbnail(marvelRealmObject.thumbnail!!.path,marvelRealmObject.thumbnail!!.extension)
    )
    return marvelObject
}

fun checkFavorite(itemId:Int?):Boolean {
    val realm = Realm.getDefaultInstance()
    val activeUser:FirebaseUser? = FirebaseAuth.getInstance().currentUser
    if (activeUser != null) {
        val dbUser = realm.where<User>().equalTo("email", activeUser.email).findFirst()
        for (x in dbUser!!.favorites) {
            if (x.id == itemId) {
                return true
            }
        }
    }
    return false
}

fun isCharacter(marvelObject: MarvelObject) : Boolean{
    if(marvelObject.title==null)return true
    else return false
}
/*
//FIREBASE database
val userListener = object : ValueEventListener {
    var concurrentUsersHashMap = HashMap<String,String>()
    override fun onDataChange(dataSnapshot: DataSnapshot) {
        val user = dataSnapshot.value
        //wth is is this
        Log.d("userListener -> onDataChange -> dataSnapshot.value", user.toString())

        if (user != null) {
            concurrentUsersHashMap = user as HashMap<String, String>
            Log.d("CURRENT USERS", concurrentUsersHashMap.toString())
        }
    }



    override fun onCancelled(databaseError: DatabaseError) {
        Log.e("userListener -> onCancelled", databaseError.toException().printStackTrace().toString())
        //do something here?
    }
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