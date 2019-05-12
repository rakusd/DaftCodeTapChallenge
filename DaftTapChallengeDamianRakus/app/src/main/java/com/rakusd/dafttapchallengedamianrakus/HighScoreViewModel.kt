package com.rakusd.dafttapchallengedamianrakus

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.SortedList

class HighScoreViewModel: ViewModel() {
    private val highScoreLiveData = MutableLiveData<MutableList<HighScore>>()

    fun getItems() : LiveData<MutableList<HighScore>> = highScoreLiveData

    fun updateList(list:MutableList<HighScore>) {
        highScoreLiveData.value=list
    }


}