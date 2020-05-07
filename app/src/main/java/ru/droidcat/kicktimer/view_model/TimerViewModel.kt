package ru.droidcat.kicktimer.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.droidcat.kicktimer.database.AppDatabase
import ru.droidcat.kicktimer.database.CyclesRepository
import ru.droidcat.kicktimer.database.model.Cycle
import java.text.SimpleDateFormat
import java.util.*
import android.os.Handler
import android.util.Log

class TimerViewModel(application: Application): AndroidViewModel(application) {

    private val cycleDAO = AppDatabase.getDatabase(application, viewModelScope).cycleDao()
    private lateinit var repository: CyclesRepository
    private lateinit var sessionId: String
    private var currentCycleId = ""
    private var timerWork = false

    private val timer = Timer()
    private var handler = Handler()

    private lateinit var updateListener: UpdateListener
    private lateinit var stateListener: TimerStateListener

    fun setSessionId(sessionId: String) {
        repository = CyclesRepository(cycleDAO, sessionId)
        this.sessionId = sessionId
    }

    fun setOnUpdateListener(updateListener: UpdateListener) {
        this.updateListener = updateListener
    }

    fun setStateListener(stateListener: TimerStateListener) {
        this.stateListener = stateListener
    }

    fun startWork() = viewModelScope.launch {
        if(currentCycleId.isNotEmpty() && !timerWork) {
            //stop Rest timer
            timer.stop()
            handler.removeCallbacks(runnable)
            repository.setCycleRestTime(currentCycleId, timer.getElapsedTimeSecs())
            Log.d("Work started", "rest: ${timer.getElapsedTimeSecs()}")
        }
        timer.reset()
        //start Work timer
        handler.post(runnable)
        timer.start()
        //callback timer state
        timerWork = true
        stateListener.stateChanged(timerWork)

        val sdf = SimpleDateFormat("dd/M/yyyy HH:mm:ss:SSS", Locale.ENGLISH)
        val cycleId = sdf.format(Date())
        currentCycleId = cycleId
        val cycle = Cycle(cycleId, 0, 0, sessionId)
        repository.insertCycle(cycle)
    }

    fun startRest() = viewModelScope.launch {
        //stop Work timer
        if(currentCycleId.isNotEmpty() && timerWork) {
            timer.stop()
            handler.removeCallbacks(runnable)
            repository.setCycleWorkTime(currentCycleId, timer.getElapsedTimeSecs())
            Log.d("Rest started", "work: ${timer.getElapsedTimeSecs()}")
        }
        timer.reset()
        handler.post(runnable)
        timer.start()
        timerWork = false
        stateListener.stateChanged(timerWork)
    }

    fun stop() = viewModelScope.launch {
        timer.stop()
        handler.removeCallbacks(runnable)
        if(timerWork) {
            //stop Work timer
            repository.setCycleWorkTime(currentCycleId, timer.getElapsedTimeSecs())
            Log.d("Timer stopped", "work: ${timer.getElapsedTimeSecs()}")
        }
        else {
            //stop Rest timer
            repository.setCycleRestTime(currentCycleId, timer.getElapsedTimeSecs())
            Log.d("Timer stopped", "rest: ${timer.getElapsedTimeSecs()}")
        }
        timer.reset()
    }

    private var runnable: Runnable = object : Runnable {
        override fun run() {
            //Update timer UI callback
            updateListener.onTimerUpdate(timer.getElapsedTimeSecs())
            handler.postDelayed(this, 100)
        }
    }

}

class Timer {
    private var startTime: Long = 0
    private var stopTime: Long = 0
    private var running: Boolean = false

    fun start() {
        this.startTime = System.currentTimeMillis()
        this.running = true
    }

    fun stop() {
        this.stopTime = System.currentTimeMillis()
        this.running = false
    }

    fun reset() {
        this.stopTime = 0
        this.startTime = 0
        this.running = false
    }

    fun getElasedTime(): Long {
        if(running) {
            return System.currentTimeMillis() - startTime
        }
        return stopTime - startTime
    }

    fun getElapsedTimeSecs(): Int {
        return (getElasedTime() / 1000).toInt()
    }

}

class UpdateListener(val updateListener: (time: Int) -> Unit) {
    fun onTimerUpdate(time: Int) = updateListener(time)
}

class TimerStateListener(val stateListener: (state: Boolean) -> Unit) {
    fun stateChanged(state: Boolean) = stateListener(state)
}