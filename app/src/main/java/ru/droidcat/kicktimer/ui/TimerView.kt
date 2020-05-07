package ru.droidcat.kicktimer.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import ru.droidcat.kicktimer.R
import ru.droidcat.kicktimer.view_model.TimerStateListener
import ru.droidcat.kicktimer.view_model.TimerViewModel
import ru.droidcat.kicktimer.view_model.UpdateListener

class TimerView: AppCompatActivity() {

    private lateinit var timerState: TextView
    private lateinit var timerText: TextView
    private lateinit var fabLeft: FloatingActionButton
    private lateinit var fabRight: FloatingActionButton
    private lateinit var timerViewModel: TimerViewModel
    private lateinit var sessionId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.timer_view)

        sessionId = intent.extras?.getString("sessionId")!!

        timerState = findViewById(R.id.timer_state)
        timerText = findViewById(R.id.timer_text)
        fabLeft = findViewById(R.id.fab_left)
        fabRight = findViewById(R.id.fab_right)

        timerViewModel = ViewModelProvider(this).get(TimerViewModel::class.java)
        timerViewModel.setSessionId(sessionId)
        timerViewModel.setOnUpdateListener(timerOnUpdateListener)
        timerViewModel.setStateListener(timerStateListener)

        fabLeft.setOnClickListener { startRest() }
        fabRight.setOnClickListener { stop() }

        startWork()
    }

    override fun onBackPressed() {
        stop()
    }

    private fun startWork() {
        timerViewModel.startWork()
    }

    private fun startRest() {
        timerViewModel.startRest()
    }

    private fun stop() {
        timerViewModel.stop()
        finish()
    }

    private val timerStateListener = TimerStateListener { state ->
        when(state) {
            true -> {
                fabLeft.setImageResource(R.drawable.ic_rest)
                fabLeft.setOnClickListener { startRest() }
                timerText.setTextColor(resources.getColor(R.color.color_primary))
                timerState.text = getString(R.string.state_working)
            }
            false -> {
                fabLeft.setImageResource(R.drawable.ic_play)
                fabLeft.setOnClickListener { startWork() }
                timerText.setTextColor(resources.getColor(R.color.color_on_surface_medium))
                timerState.text = getString(R.string.state_resting)
            }
        }
    }

    private val timerOnUpdateListener = UpdateListener { time ->
        val hours = time / 3600
        val minutes = time / 60 - hours*60
        val seconds = time - minutes*60 - hours*3600
        var text = ""
        if(hours > 0) {
            text = if(hours >= 10) "${hours}:" else "0${hours}:"
        }
        if(minutes > 0) {
            text += if(minutes >= 10) "${minutes}:" else "0${minutes}:"
        }
        text += if(seconds >= 10) "${seconds}" else "0${seconds}"

        timerText.text = text
    }
}