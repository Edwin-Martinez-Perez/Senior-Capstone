package com.example.collectivetrek

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.collectivetrek.databinding.ItineraryEventItemBinding
import com.example.collectivetrek.database.Event


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
    }


    class EventItineraryViewHolder(var binding: ItineraryEventItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            event: Event,
            clickListener: EventItineraryListener
        )  {

            binding.eventData = event
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
    fun onClick(event: Event) = clickListener(event)
}