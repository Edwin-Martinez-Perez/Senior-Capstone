package com.example.collectivetrek

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.collectivetrek.database.Group
import com.example.collectivetrek.generated.callback.OnClickListener

class MyAdapter(val clickListener: GroupFragmentListener, private val group : ArrayList<Group>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.individual_group,
            parent,false)
        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val current_item = group[position]

        holder.groupName.text = current_item.groupName
        holder.date.text = current_item.date
        holder.destination.text = current_item.destination

        holder.bind(current_item, clickListener)

    }

    override fun getItemCount(): Int {

        return group.size
    }


    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val groupName : TextView = itemView.findViewById(R.id.group_name)
        val destination : TextView = itemView.findViewById(R.id.destination)
        val date : TextView = itemView.findViewById(R.id.date)

        fun bind(
            group: Group,
            clickListener: GroupFragmentListener
        ){
            itemView.setOnClickListener{
                clickListener.onClick(group)

            }
        }


    }

}
class GroupFragmentListener(val clickListener:(group: Group) -> Unit){
    fun onClick(group:Group) {
        clickListener(group)
    }
}