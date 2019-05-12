package com.rakusd.dafttapchallengedamianrakus

import android.graphics.Matrix
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.Settings
import android.view.MotionEvent
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_game.*
import org.jetbrains.anko.doAsync
import java.io.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class GameActivity : AppCompatActivity() {

    private lateinit var highScores:MutableList<HighScore>
    private var score = 0
    private var gameOn = false
    private val gameTimer:CountDownTimer by lazy {
        object:CountDownTimer(5000,1000) {
            override fun onFinish() {
                gameOn=false
                endGame()
            }

            override fun onTick(millisUntilFinished: Long) {
                timeTextView.text=getString(R.string.timeLeftStr)+(millisUntilFinished/1000).toString()
            }
        }
    }

    private val startTimer:CountDownTimer by lazy {
        object:CountDownTimer(4000,1000) {
            override fun onFinish() {
                startTextView.text=getString(R.string.play)
                gameOn=true
                gameTimer.start()
            }

            override fun onTick(millisUntilFinished: Long) {
                startTextView.text=(millisUntilFinished/1000).toString()+"..."
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        doAsync {
            readScoresFromFile()
        }
    }

    private fun endGame() {
        if(highScores.size >= 5 && !highScores.any { it.score < score }) {
            AlertDialog.Builder(this)
                .setTitle("Nice job")
                .setMessage("Your score: $score")
                .setPositiveButton("Go back",null)
                .setCancelable(false)
                .setOnDismissListener { finish() }
                .show()
        } else {
            val place = addHighScore()
            doAsync { savesScoresToFile() }
            AlertDialog.Builder(this)
                .setTitle("Holy moly !")
                .setMessage("Congratulations! You scored $score points and earned place #${place+1}")
                .setPositiveButton("Go back",null)
                .setCancelable(false)
                .setOnDismissListener { finish() }
                .show()
        }
    }

    private fun addHighScore():Int {
        if(highScores.size>=5) {
            repeat(highScores.size-4) { highScores.removeAt(highScores.size-1) }
        }
        val date = Date(System.currentTimeMillis())
        val record = HighScore(score,date)
        highScores.add(record)
        highScores.sortByDescending { it->it.score }

        return highScores.indexOf(record)
    }

    private fun readScoresFromFile() {
        val file = File(filesDir,getString(R.string.scoreFile))
        if(!file.exists()) {
            highScores = mutableListOf()
            return
        }
        val input = ObjectInputStream(FileInputStream(file))

        val scores = mutableListOf<HighScore>()
        var stop=false
        while(!stop) {
            try {
                val obj:Any?=input.readObject()
                if(obj==null) {
                    stop=true
                } else {
                    val item = obj as HighScore
                    scores.add(item)
                }
            } catch (e: Exception) {
                stop = true
            }
        }
        highScores=scores
        input.close()
    }

    private fun savesScoresToFile() {
        val file = File(filesDir,getString(R.string.scoreFile))
        val output = ObjectOutputStream(FileOutputStream(file))

        for(item in highScores) {
            output.writeObject(item)
        }
        output.close()
    }

    override fun onResume() {
        super.onResume()
        startTimer.start()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(gameOn && event?.action == MotionEvent.ACTION_UP) {
            score++
            scoreTextView.text="Score: "+score.toString()
        }
        return true
    }
}
