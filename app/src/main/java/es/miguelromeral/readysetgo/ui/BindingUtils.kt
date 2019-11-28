package es.miguelromeral.readysetgo.ui

import android.widget.TextView
import androidx.databinding.BindingAdapter
import es.miguelromeral.readysetgo.R
import es.miguelromeral.readysetgo.ui.database.Start
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("timeFormatted")
fun TextView.setTimeFormatted(item: Start) {
    item?.let {
        text = formatTime(item.time)
    }
}

@BindingAdapter("dateFormatted")
fun TextView.setDateFormatted(item: Start) {
    item?.let {
        text = convertLongToTime(item.date)
    }
}

@BindingAdapter("waitingFormatted")
fun TextView.setWaitingFormatted(item: Start) {
    item?.let{
        text = context.resources.getString(R.string.details_waiting_time) + " " + formatTime(item.waitingTime)
    }
}


@BindingAdapter("stepFormatted")
fun TextView.setStepFormatted(item: Start) {
    item?.let{
        text = context.resources.getString(R.string.step_time) + " " + formatTime(item.stepTime)
    }
}


@BindingAdapter("maxWaitingTimeFormatted")
fun TextView.setMaxWaitingTimeFormatted(item: Start) {
    item?.let{
        text = context.resources.getString(R.string.max_wait_time_formatted) + " " + formatTime(item.maxWaitTime)
    }
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