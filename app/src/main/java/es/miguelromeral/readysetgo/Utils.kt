package es.miguelromeral.readysetgo

import es.miguelromeral.readysetgo.ui.formatTime

fun getDifferenceBetween(t1: Long, t2: Long): String
{
    if(t1 < t2){
        return "+" + formatTime(t2 - t1)
    }else if(t1 > t2){
        return "-" + formatTime(t1 - t2)
    }else{
        return "+0.000"
    }
}