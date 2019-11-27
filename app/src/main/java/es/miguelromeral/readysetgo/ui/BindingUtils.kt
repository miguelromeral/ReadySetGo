package es.miguelromeral.readysetgo.ui

import android.widget.TextView
import androidx.databinding.BindingAdapter
import es.miguelromeral.readysetgo.R
import es.miguelromeral.readysetgo.ui.database.Start
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("timeFormatted")
fun TextView.setTimeFormatted(item: Start) {
    text = formatTime(item.time)
}

@BindingAdapter("dateFormatted")
fun TextView.setDateFormatted(item: Start) {
    text = convertLongToTime(item.date)
}

@BindingAdapter("waitingFormatted")
fun TextView.setWaitingFormatted(item: Start) {
    text = context.resources.getString(R.string.details_waiting_time) + " " + formatTime(item.waitingTime)
}




fun formatTime(time: Long): String{
    val dou = time.toDouble() / 1000
    return dou.format(3)
}

fun convertLongToTime(time: Long): String {
    val date = Date(time)
    val format = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
    return format.format(date)
}

fun Double.format(digits: Int) = "%.${digits}f".format(this)