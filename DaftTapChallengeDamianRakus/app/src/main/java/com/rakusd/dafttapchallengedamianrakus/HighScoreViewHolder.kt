package com.rakusd.dafttapchallengedamianrakus

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.highscore_layout.view.*
import java.text.SimpleDateFormat

class HighScoreViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
    val place = itemView.placeTextView
    val score = itemView.scoreTextView
    val time = itemView.timeTextView

    fun bindHeader() {
        place.text="Place"
        score.text="Score"
        time.text="Date"
    }
    fun bindItem(highScore:HighScore,pos:Int) {
        place.text=(pos+1).toString()+"."
        score.text=highScore.score.toString()
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        time.text=formatter.format(highScore.time)
    }
}