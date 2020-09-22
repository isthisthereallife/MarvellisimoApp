package isthisstuff.practice.marvellisimohdd.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import isthisstuff.practice.marvellisimohdd.MyActiveUsersViewHolder
import isthisstuff.practice.marvellisimohdd.R

class ActiveUsersAdapter() : RecyclerView.Adapter<MyActiveUsersViewHolder>() {
    lateinit var itemEmail: TextView

    var data: List<String> = listOf("KOLLA","HÄRA","LISTAN","FUNKAR")
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyActiveUsersViewHolder {
        Log.d("ActiveUsersAdapter->onCreateViewHolder","Wow, en ViewHolder!")
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_active_user,parent,false) as LinearLayout
        return MyActiveUsersViewHolder(view)
    }
    override fun onBindViewHolder(holder : MyActiveUsersViewHolder, position: Int){
        val item = data[position]
        Log.d("ActiveUsersAdapter->onBindViewHolder","Wow, ett element!")

        holder.view.findViewById<TextView>(R.id.user_email_text_view).text = item
        holder.view.findViewById<LinearLayout>(R.id.active_user_item).setOnClickListener{
            //TODO HÄR SKA item (som är en email) bli Receiver i en sånhärn: /**val message = "SENDER<${removeDotFromEmail(sender)}>RECEIVER<${removeDotFromEmail(target)}>TIME<$timestring>"
            //        Log.d("MESSAGE",message)
            //        val myReference = database.getReference(message)**/


            //och kanske göra en toast om att det skickats
        }

    }

}