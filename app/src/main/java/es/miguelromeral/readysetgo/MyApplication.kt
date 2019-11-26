package es.miguelromeral.readysetgo

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Transformations
import androidx.preference.PreferenceManager

class MyApplication : Application() {

    companion object {

        val SP_APPSTYLE = "appStyle"

        lateinit var myPreferences : SharedPreferences

        fun changeStyle(){
            val style = myPreferences.getBoolean(SP_APPSTYLE, false)
            if(style){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    override fun onCreate(){
        super.onCreate()
        myPreferences = PreferenceManager.getDefaultSharedPreferences(this )
    }
}