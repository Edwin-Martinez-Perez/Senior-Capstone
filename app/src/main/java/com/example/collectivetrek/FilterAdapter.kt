package com.example.collectivetrek



import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.collectivetrek.database.Filter
import com.example.collectivetrek.databinding.ItineraryEventFilterBinding


class FilterAdapter(val clickListener: FilterItineraryListener) :
    ListAdapter<Filter, FilterAdapter.FilterItineraryViewHolder>(EventsComparator()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : FilterItineraryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return FilterItineraryViewHolder(
            ItineraryEventFilterBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: FilterItineraryViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current, clickListener)
    }


    class FilterItineraryViewHolder(var binding: ItineraryEventFilterBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            filter: Filter,
            clickListener: FilterItineraryListener
        )  {
            binding.filterData = filter
            binding.filterClickListener = clickListener
            binding.executePendingBindings()
            binding.root.setOnClickListener {
                clickListener.onClick(filter)
                Log.d("clicked", itemView.toString())
//                // TODO when the app opened, first filter should be selected
//                // TODO when other filter is clicked, previous filtere should change colors
//                binding.filterCard.setBackgroundResource(R.drawable.itinerary_filter_clicked)

            }

        }
    }

    class EventsComparator : DiffUtil.ItemCallback<Filter>() {
        override fun areItemsTheSame(oldItem: Filter, newItem: Filter): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Filter, newItem: Filter): Boolean {
            return oldItem.name == newItem.name
        }
    }
}

class FilterItineraryListener(val clickListener: (filter: Filter) -> Unit) {
    fun onClick(filter: Filter) = clickListener(filter)
}