package com.example.collectivetrek

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.collectivetrek.database.Group

class MyAdapter(private val group : ArrayList<Group>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.individual_group,
            parent,false)
        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val current_item = group[position]

        holder.groupName.text = current_item.groupName
        holder.date.text = current_item.destination
        holder.destination.text = current_item.date

    }

    override fun getItemCount(): Int {

        return group.size
    }


    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val groupName : TextView = itemView.findViewById(R.id.group_name)
        val destination : TextView = itemView.findViewById(R.id.destination)
        val date : TextView = itemView.findViewById(R.id.date)

    }

}