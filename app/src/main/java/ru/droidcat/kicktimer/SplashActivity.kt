package ru.droidcat.kicktimer

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.lang.Thread.sleep

class SplashActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sleep(500)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}