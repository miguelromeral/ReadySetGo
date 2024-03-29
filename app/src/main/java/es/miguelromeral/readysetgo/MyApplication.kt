package es.miguelromeral.readysetgo

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Transformations
import androidx.preference.PreferenceManager
import es.miguelromeral.readysetgo.MyApplication.Companion.myPreferences

class MyApplication : Application() {

    companion object {

        lateinit var myPreferences : SharedPreferences
        lateinit var allResources : Resources

        fun changeStyle(){
            val style = myPreferences.getBoolean(allResources.getString(R.string.preference_id_appstyle), false)
            if(style){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        fun getPreferenceMaxWait() = myPreferences.getInt(allResources.getString(R.string.preference_id_maxwait), 2000).toLong()

        fun getPreferenceSecondDuration() = myPreferences.getInt(allResources.getString(R.string.preference_id_secondduration), 1000).toLong()
        fun getPreferenceDateFormat() = myPreferences.getString(
            allResources.getString(R.string.preference_id_date_format),
            allResources.getString(R.string.date_format_one))

        fun getPreferenceGameMode(): String {
            val default = allResources.getString(R.string.preference_game_mode_one)
            val set = myPreferences.getString(
                allResources.getString(R.string.preference_id_game_mode),
                default)
            set?.let{
                return it
            }
            return default
        }
    }

    override fun onCreate(){
        super.onCreate()
        allResources = resources
        myPreferences = PreferenceManager.getDefaultSharedPreferences(this )
    }
}