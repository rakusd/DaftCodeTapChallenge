package com.rakusd.dafttapchallengedamianrakus

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class HighScoreAdapter(var items:MutableList<HighScore>): RecyclerView.Adapter<HighScoreViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HighScoreViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.highscore_layout,parent,false)
        return HighScoreViewHolder(view)
    }

    override fun getItemCount(): Int = items.size+1

    override fun onBindViewHolder(holder: HighScoreViewHolder, position: Int) {
        if(position==0) {
            holder.bindHeader()
        } else {
            holder.bindItem(items[position-1],position-1)
        }

    }
}