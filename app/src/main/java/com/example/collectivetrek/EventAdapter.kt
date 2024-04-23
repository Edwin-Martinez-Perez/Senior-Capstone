package com.example.collectivetrek

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.collectivetrek.database.Event
import com.example.collectivetrek.databinding.ItineraryEventItemBinding

interface EventAdapterCallback {
    fun onAddressClick(address: String)
}

interface EventAdapterDeleteCallback {
    fun onDeleteEventClick(event: Event, deleteButton: ImageButton)
}
class EventAdapter(val clickListener: EventItineraryListener, private val callback: EventAdapterCallback, private val callbackDelete: EventAdapterDeleteCallback) :
    ListAdapter<Event, EventAdapter.EventItineraryViewHolder>(EventsComparator()){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : EventItineraryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return EventItineraryViewHolder(
            ItineraryEventItemBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: EventItineraryViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current, clickListener)
        holder.binding.eventImage.visibility = View.GONE
        holder.binding.addressText.setOnClickListener {
            Log.d("addressText",holder.binding.addressText.text.toString())
            callback.onAddressClick(holder.binding.addressText.text.toString())
        }
        holder.binding.deleteButton.setOnClickListener {
            Log.d("deleteButton", "clicked")

            callbackDelete.onDeleteEventClick(current, holder.binding.deleteButton)
        }
        if  (current.date == null){
            holder.binding.eventDateText.visibility = View.GONE
        } else {
            holder.binding.eventDateText.visibility = View.VISIBLE
        }
        //val bitmap = stringToBitmap(current.bitmap)
        if (current.bitmap != null) {
            val bitmap = stringToBitmap(current.bitmap)
            Log.d("bitmap not null", bitmap.toString())
            Log.d("bitmap not null", holder.binding.eventImage.toString())
            holder.binding.eventImage.visibility = View.VISIBLE
            holder.binding.eventImage.setImageBitmap(bitmap)
        } else {
            Log.d("bitmap null", holder.binding.eventImage.toString())
        }

    }

    fun stringToBitmap(encodedString: String?) : Bitmap?{
        if (encodedString != null) {
            val decodedBytes = Base64.decode(encodedString, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } else {
            return null
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