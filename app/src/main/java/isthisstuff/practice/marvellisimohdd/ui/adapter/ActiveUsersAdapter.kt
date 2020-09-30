package isthisstuff.practice.marvellisimohdd.ui.adapter

import java.util.*
import android.util.Log
import android.widget.Button
import android.view.ViewGroup
import android.widget.TextView
import java.time.LocalDateTime
import android.widget.LinearLayout
import android.view.LayoutInflater
import isthisstuff.practice.marvellisimohdd.R
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import isthisstuff.practice.marvellisimohdd.isCharacter
import isthisstuff.practice.marvellisimohdd.entities.MarvelObject
import isthisstuff.practice.marvellisimohdd.MyActiveUsersViewHolder

class ActiveUsersAdapter(_sender: String, _marvelObject: MarvelObject) :
    RecyclerView.Adapter<MyActiveUsersViewHolder>() {
    private var database = FirebaseDatabase.getInstance()
    var sender = _sender
    var marvelObject = _marvelObject

    var data = listOf<String>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyActiveUsersViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_active_user, parent, false) as LinearLayout
        return MyActiveUsersViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyActiveUsersViewHolder, position: Int) {
        val item = data[position]
        holder.view.findViewById<TextView>(R.id.user_email_text_view).text =
            item.replace(",", ".")
        holder.view.findViewById<Button>(R.id.button_send_to_user).setOnClickListener {
            sendMessage(
                sender = sender,
                receiver = item,
                payload = marvelObject
            )

            val updatedText = "Sent to ${item.replace(",",".")}"

            holder.view.findViewById<TextView>(R.id.user_email_text_view).text = updatedText
        }

    }

    private fun sendMessage(sender: String, receiver: String, payload: MarvelObject) {
        val receiverNoDots = receiver.replace(".", ",")
        val customDatabaseMessageReference =
            database.getReference("<TO:${receiverNoDots}>")
        var timeString = ""
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            timeString = LocalDateTime.now().toString().replace(".", ":")
        } else {
            Log.d("YOUR API IS WAY OLD", Calendar.getInstance().toString())
            timeString = Calendar.getInstance().toString()
        }

        val type = if (isCharacter(payload)) "character"
        else "series"

        val name = if (type == "character")
            marvelObject.name.toString()
        else
            marvelObject.title.toString()


        val message =
            "<SENDER>$sender</SENDER><PAYLOAD>$payload</PAYLOAD><MARVELOBJECTNAME>$name</MARVELOBJECTNAME><MARVELTYPE>$type</MARVELTYPE><TIMESTAMP>$timeString</TIMESTAMP>"

        customDatabaseMessageReference.push().setValue(message)


    }

}