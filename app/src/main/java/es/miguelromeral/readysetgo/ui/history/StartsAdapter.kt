package es.miguelromeral.readysetgo.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import es.miguelromeral.readysetgo.R
import es.miguelromeral.readysetgo.ui.database.Start
import kotlinx.android.synthetic.main.list_item_start.view.*
import java.text.SimpleDateFormat
import java.util.*


class StartsAdapter : RecyclerView.Adapter<StartsAdapter.ViewHolder>()
{
    var data = listOf<Start>()
        set(value){
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        val item = data[position]
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tv_date: TextView = itemView.findViewById(R.id.item_date)
        val tv_time: TextView = itemView.findViewById(R.id.item_time)

        fun bind(item: Start){
            tv_date.text = convertLongToTime(item.date)
            tv_time.text = item.time.toString()
        }

        private fun convertLongToTime(time: Long): String {
            val date = Date(time)
            val format = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
            return format.format(date)
        }

        companion object{
            fun from(parent: ViewGroup): ViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.list_item_start, parent, false)
                return ViewHolder(view)
            }
        }
    }
}



