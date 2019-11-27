package es.miguelromeral.readysetgo.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import es.miguelromeral.readysetgo.R
import es.miguelromeral.readysetgo.databinding.ListItemStartBinding
import es.miguelromeral.readysetgo.generated.callback.OnClickListener
import es.miguelromeral.readysetgo.ui.convertLongToTime
import es.miguelromeral.readysetgo.ui.database.Start
import kotlinx.android.synthetic.main.list_item_start.view.*
import java.text.SimpleDateFormat
import java.util.*


class StartsAdapter(val clickListener: StartListener) :
    ListAdapter<Start, StartsAdapter.ViewHolder>(StartDiffCallback())
{

    override fun onBindViewHolder(holder: StartsAdapter.ViewHolder, position: Int){
        val item = getItem(position)
        holder.bind(getItem(position)!!, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StartsAdapter.ViewHolder {
        return StartsAdapter.ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemStartBinding) : RecyclerView.ViewHolder(binding.root)
    {

        fun bind(item: Start, clickListener: StartListener){
            binding.record = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object{
            fun from(parent: ViewGroup): ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemStartBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class StartDiffCallback : DiffUtil.ItemCallback<Start>() {
    override fun areItemsTheSame(oldItem: Start, newItem: Start): Boolean {
        return oldItem.startId == newItem.startId
    }

    override fun areContentsTheSame(oldItem: Start, newItem: Start): Boolean {
        return oldItem == newItem
    }
}


class StartListener(val clickListener: (item: Start) -> Unit){
    fun onClick(start: Start) = clickListener(start)
}
