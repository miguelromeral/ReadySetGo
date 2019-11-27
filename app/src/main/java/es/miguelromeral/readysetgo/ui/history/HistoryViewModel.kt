package es.miguelromeral.readysetgo.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import es.miguelromeral.readysetgo.R
import es.miguelromeral.readysetgo.ui.database.ApplicationDatabaseDao
import es.miguelromeral.readysetgo.ui.database.Start

class HistoryViewModel(
    val database: ApplicationDatabaseDao,
    application: Application) : AndroidViewModel(application) {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text


    val bestRecord = database.getBestStart()

    val startRecords = database.getAllStarts()


    private var _bestText = MutableLiveData<String>().apply {
        value = application.resources.getString(R.string.no_time_set_yet)
    }
    val bestText: LiveData<String> = _bestText
}