package com.example.collectivetrek

import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.collectivetrek.databinding.ItineraryEventItemBinding
import com.example.collectivetrek.database.Event

interface EventAdapterCallback {
    fun onAddressClick(address: String)
}
class EventAdapter(val clickListener: EventItineraryListener, private val callback: EventAdapterCallback) :
    ListAdapter<Event, EventAdapter.EventItineraryViewHolder>(EventsComparator()){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : EventItineraryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

class EventAdapter(val clickListener: EventItineraryListener) :
    ListAdapter<Event, EventAdapter.EventItineraryViewHolder>(EventsComparator()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : EventItineraryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        //return WordViewHolder(view)

        return EventItineraryViewHolder(
            ItineraryEventItemBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: EventItineraryViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current, clickListener)
        holder.binding.addressText.setOnClickListener {
            Log.d("addressText",holder.binding.addressText.text.toString())
            callback.onAddressClick(holder.binding.addressText.text.toString())
        }
    }


    class EventItineraryViewHolder(var binding: ItineraryEventItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            event: Event,
            clickListener: EventItineraryListener
        )  {

            binding.eventData = event
            //binding.addressText.text = Html.fromHtml("<u>${event.address}</u>")
            binding.addressText.paintFlags = binding.addressText.paintFlags or Paint.UNDERLINE_TEXT_FLAG

            binding.clickListener = clickListener
            binding.executePendingBindings()
            binding.root.setOnClickListener {
                clickListener.onClick(event)
            }
        }

    }
    class EventsComparator : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.placeName == newItem.placeName
        }
    }


}

class EventItineraryListener(val clickListener: (event: Event) -> Unit) {
    //fun onClick(event: Event) = clickListener(event)
    fun onClick(event: Event) {
        clickListener(event)
    }
}

class EventItineraryListener(val clickListener: (event: Event) -> Unit) {
    fun onClick(event: Event) = clickListener(event)
}