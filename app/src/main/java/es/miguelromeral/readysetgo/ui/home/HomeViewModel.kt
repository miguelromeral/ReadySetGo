package es.miguelromeral.readysetgo.ui.home

import android.app.Application
import android.content.Context
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import androidx.preference.PreferenceManager

import es.miguelromeral.readysetgo.R
import es.miguelromeral.readysetgo.MyApplication
import es.miguelromeral.readysetgo.ui.database.ApplicationDatabaseDao
import es.miguelromeral.readysetgo.ui.database.ReadySetGoDatabase
import es.miguelromeral.readysetgo.ui.database.Start
import es.miguelromeral.readysetgo.ui.home.HomeViewModel.Companion.COUNTDOWN_LAUNCHED
import es.miguelromeral.readysetgo.ui.home.HomeViewModel.Companion.COUNTDOWN_NO_STARTED
import kotlinx.coroutines.*
import kotlin.random.Random

class HomeViewModel(
    val database: ApplicationDatabaseDao,
    application: Application) : AndroidViewModel(application) {

    companion object{
        val COUNTDOWN_NO_STARTED = -1
        val COUNTDOWN_LAUNCHED = 0

        const val DEFAULT_SECOND_DURATION = 1000L
        const val DEFAULT_MAX_WAIT = 2000L

        const val NO_SCORE = 0L
    }

    private var preferenceCountdownMaxWait: Long = DEFAULT_MAX_WAIT
    private var preferenceOneSecondDuration: Long = DEFAULT_SECOND_DURATION
    private var timeToTurnOn = preferenceOneSecondDuration * 5


    private var _countdown = MutableLiveData<Int>()
    val countdown: LiveData<Int>
        get() = _countdown

    private val _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<Long>
        get() = _currentTime

    private lateinit var timer: CountDownTimer

    private var randomWait: Long = 2000L

    private var timestamp: Long?

    private val _score = MutableLiveData<Long?>()
    val score : LiveData<Long?>
        get() = _score


    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val bestRecord = database.getBestStart()

    init{
        _countdown.value = COUNTDOWN_NO_STARTED
        timestamp = null
        _score.value = NO_SCORE
    }

    fun initSettings(){
        preferenceCountdownMaxWait = MyApplication.getPreferenceMaxWait()
        preferenceOneSecondDuration = MyApplication.getPreferenceSecondDuration()
        timeToTurnOn = preferenceOneSecondDuration * 5
    }

    private fun initTimer(){
        randomWait = timeToTurnOn + 200L + Random.nextLong(preferenceCountdownMaxWait)
        timer = object : CountDownTimer(randomWait, preferenceOneSecondDuration){
            override fun onTick(milisUtilFinished: Long){
                _countdown.value = ((randomWait - milisUtilFinished) / preferenceOneSecondDuration).toInt() + 1
            }

            override fun onFinish() {
                _countdown.value = COUNTDOWN_LAUNCHED
                timestamp = System.currentTimeMillis()
            }
        }
    }

    fun layoutTouched(){
        val end = System.currentTimeMillis()
        when(countdown.value){
            COUNTDOWN_NO_STARTED -> {
                initTimer()
                timer.start()
                timestamp = null
            }
            COUNTDOWN_LAUNCHED -> {
                if(timestamp != null) {
                    _countdown.value = COUNTDOWN_NO_STARTED
                    val result = end - timestamp as Long
                    _score.value = result
                    uiScope.launch {
                        createStartRecord(end, result, randomWait)
                    }
                }
            }
            else -> {
                timer.cancel()
                _countdown.value = COUNTDOWN_NO_STARTED
            }
        }
    }

    private suspend fun createStartRecord(date: Long, time:Long, waiting: Long){
        return withContext(Dispatchers.IO){
            var record = Start(0L, date, time, waiting)
            database.insert(record)
        }
    }

    override fun onCleared() {
        super.onCleared()
        // …
        viewModelJob.cancel()
    }

}