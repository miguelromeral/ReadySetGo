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

    private var _semaphoreCount = 5
    val semaphoreCount: Int
        get() = _semaphoreCount

    private var preferenceCountdownMaxWait: Long = DEFAULT_MAX_WAIT
    private var preferenceOneSecondDuration: Long = DEFAULT_SECOND_DURATION
    private var timeToTurnOn = preferenceOneSecondDuration * semaphoreCount

    private var _gameMode = MutableLiveData<String>()
    val gameMode: LiveData<String>
        get() = _gameMode

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

    val bestRecord: LiveData<Start> by lazy {
        database.getBestStart()
    }


    init{
        _countdown.value = COUNTDOWN_NO_STARTED
        timestamp = null
        _score.value = NO_SCORE
    }

    fun initSettings(){
        _gameMode.value = MyApplication.getPreferenceGameMode()

        _semaphoreCount = when(gameMode.value){
            MyApplication.allResources.getString(R.string.preference_game_mode_one) -> 5
            MyApplication.allResources.getString(R.string.preference_game_mode_two) -> 4
            else -> 5
        }

        preferenceCountdownMaxWait = MyApplication.getPreferenceMaxWait()
        preferenceOneSecondDuration = MyApplication.getPreferenceSecondDuration()
        timeToTurnOn = preferenceOneSecondDuration * 5
    }

    private fun initTimer(){
        randomWait = timeToTurnOn + Random.nextLong(preferenceCountdownMaxWait)
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

    fun suspendTreat(){
        _countdown.value = COUNTDOWN_NO_STARTED
        timestamp = null
        timer.cancel()
    }

    private suspend fun createStartRecord(date: Long, time:Long, waiting: Long){
        return withContext(Dispatchers.IO){
            var record = Start(0L,
                date = date,
                time = time,
                waitingTime = waiting,
                stepTime = preferenceOneSecondDuration,
                maxWaitTime = preferenceCountdownMaxWait
                ,gameMode = ReadySetGoDatabase.getGameModeFromString(gameMode.value)
            )
            database.insert(record)
        }
    }

    override fun onCleared() {
        super.onCleared()
        // …
        viewModelJob.cancel()
    }

}