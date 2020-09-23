package isthisstuff.practice.marvellisimohdd.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import isthisstuff.practice.marvellisimohdd.MyActiveUsersViewHolder
import isthisstuff.practice.marvellisimohdd.R
import java.time.LocalDateTime

class ActiveUsersAdapter() : RecyclerView.Adapter<MyActiveUsersViewHolder>() {
    lateinit var itemEmail: TextView
    private var database = FirebaseDatabase.getInstance()
    private var databaseMessageReference = database.getReference("messages")

    var data = listOf<Pair<String, String>>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyActiveUsersViewHolder {
        Log.d("ActiveUsersAdapter->onCreateViewHolder", "Wow, en ViewHolder!")
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_active_user, parent, false) as LinearLayout
        return MyActiveUsersViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyActiveUsersViewHolder, position: Int) {
        val item = data[position]
        Log.d(
            "HÄÄÄÄÄÄÄÄÄÄÄÄÄÄÄR !!!!!!!!!! ¤#/&¤/#(&¤/ ActiveUsersAdapter->onBindViewHolder",
            item.second.toString()
        )

        Log.d("ActiveUsersAdapter->onBindViewHolder", "Wow, ett element! $item")

        holder.view.findViewById<TextView>(R.id.user_email_text_view).text =
            item.second.replace(",", ".")
        holder.view.findViewById<LinearLayout>(R.id.active_user_item).setOnClickListener {

            //        val myReference = database.getReference(message)**/
            val timeString = LocalDateTime.now().toString().replace(".", ":")

            val message =
                "SENDER<${"TODO put sender here "}>RECEIVER<${item.second}>TIME<$timeString>PAYLOAD:SERIES_ID<Todo skicka med ett id>"

            databaseMessageReference.push().setValue(message)


            val updatedText = "Skickat till ${item.second}"
            holder.view.findViewById<TextView>(R.id.user_email_text_view).text = updatedText

            //och kanske göra en toast om att det skickats

        }

    }

}