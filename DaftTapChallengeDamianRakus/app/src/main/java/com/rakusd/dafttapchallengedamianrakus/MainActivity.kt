package com.rakusd.dafttapchallengedamianrakus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import java.io.File
import java.io.FileInputStream
import java.io.ObjectInputStream
import java.lang.Exception


class MainActivity : AppCompatActivity() {
    private val viewModel:HighScoreViewModel by lazy { ViewModelProviders.of(this).get(HighScoreViewModel::class.java)}
    private val adapter by lazy {HighScoreAdapter(mutableListOf())}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playButton.setOnClickListener {
            val intent = Intent(this,GameActivity::class.java)
            startActivity(intent)
        }

        highScoreList.adapter=adapter
        viewModel.getItems().observe(this, Observer(this::updateList))
    }

    override fun onResume() {
        super.onResume()
        doAsync {
            readScoreFromFile()
        }
    }

    private fun readScoreFromFile() {
        val file = File(filesDir,getString(R.string.scoreFile))
        if(!file.exists())
            return
        val input = ObjectInputStream(FileInputStream(file))

        val scores = mutableListOf<HighScore>()
        var stop=false
        while(!stop) {
            try {
                var obj:Any?=input.readObject()
                if(obj==null) {
                    stop=true
                } else {
                    val item = obj as HighScore
                    scores.add(item)
                }
            } catch (e:Exception) {
                stop = true
            }
        }

        runOnUiThread {
            viewModel.updateList(scores)
        }
        input.close()

    }

    private fun updateList(list: MutableList<HighScore>?) {
        if(list==null)
            return
        adapter.items=list
        adapter.notifyDataSetChanged()
    }
}
