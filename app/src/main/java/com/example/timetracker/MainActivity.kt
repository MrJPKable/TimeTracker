package com.example.timetracker

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer
import java.util.Calendar

class MainActivity : AppCompatActivity() {
    private val timeTrackerSave = "SaveData"
    private lateinit var chronometerD: Chronometer
    private lateinit var chronometerW: Chronometer
    private lateinit var chronometerM: Chronometer
    private lateinit var chronometerT: Chronometer

    private var timeStamp = Calendar.getInstance()


    /*
    NEXT: use start and stop List to store data:
    val startList = mutableListOf<Int>()
    val stopList = mutableListOf<Int>()
    than append when pressing start or stop:
    startList.add(time)
    stopList.add(tim)
    */

    //Load Data:

    private fun loadData(){
        val sharedPref: SharedPreferences = getSharedPreferences(timeTrackerSave, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        chronometerD.base = System.currentTimeMillis() - sharedPref.getLong("chronometer_d",0)
        chronometerW.base = System.currentTimeMillis() - sharedPref.getLong("chronometer_w",0)
        chronometerM.base = System.currentTimeMillis() - sharedPref.getLong("chronometer_m",0)
        chronometerT.base = System.currentTimeMillis() - sharedPref.getLong("chronometer_t",0)
        timeStamp.timeInMillis = sharedPref.getLong("timeStamp",0)
        editor.apply()
    }
    //Save Data:
    private fun saveData(){
        val sharedPref: SharedPreferences = getSharedPreferences(timeTrackerSave, MODE_PRIVATE)
        val editor = sharedPref.edit()
        checkTimer()
        editor.putLong("chronometer_d", System.currentTimeMillis()-chronometerD.base)
        editor.putLong("chronometer_w", System.currentTimeMillis()-chronometerW.base)
        editor.putLong("chronometer_m", System.currentTimeMillis()-chronometerM.base)
        editor.putLong("chronometer_t", System.currentTimeMillis()-chronometerT.base)
        editor.putLong("timeStamp", System.currentTimeMillis())
        editor.apply()
    }

    private fun checkTimer(){
        val changedYear = (Calendar.getInstance().get(Calendar.YEAR) != timeStamp.get(Calendar.YEAR))
        if ((Calendar.getInstance().get(Calendar.DAY_OF_YEAR) != timeStamp.get(Calendar.DAY_OF_YEAR)) or changedYear){
            chronometerD.base = SystemClock.elapsedRealtime()
            if ((Calendar.getInstance().get(Calendar.WEEK_OF_YEAR) != timeStamp.get(Calendar.WEEK_OF_YEAR)) or changedYear){
                chronometerW.base = SystemClock.elapsedRealtime()
            }
            if ((Calendar.getInstance().get(Calendar.MONTH) != timeStamp.get(Calendar.MONTH)) or changedYear){
                chronometerM.base = SystemClock.elapsedRealtime()
            }
        }
    }

    private fun startChronometers(){
        loadData()
        chronometerD.start()
        chronometerW.start()
        chronometerM.start()
        chronometerT.start()
    }
    private fun stopChronometers(){
        chronometerD.stop()
        chronometerW.stop()
        chronometerM.stop()
        chronometerT.stop()
        saveData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chronometerD = findViewById(R.id.ChronometerDay)
        chronometerW = findViewById(R.id.ChronometerWeek)
        chronometerM = findViewById(R.id.ChronometerMonth)
        chronometerT = findViewById(R.id.ChronometerTotal)

        val startstopbutton = findViewById<Button>(R.id.StartStopButton)
        val resetbutton = findViewById<Button>(R.id.ResetButton)
        var isRunning = false

        resetbutton.setOnClickListener {
            chronometerD.base = SystemClock.elapsedRealtime()
            chronometerW.base = SystemClock.elapsedRealtime()
            chronometerM.base = SystemClock.elapsedRealtime()
            chronometerT.base = SystemClock.elapsedRealtime()
            saveData()
        }

        startstopbutton.setOnClickListener {
            if (isRunning) {
                // Stop the chronometers
                stopChronometers()
                startstopbutton.text = "Start"
                saveData()
            } else {
                // Start or resume the chronometers
                loadData()
                startChronometers()
                startstopbutton.text = "Stop"
            }
            isRunning = !isRunning
        }
        loadData()
    }
}