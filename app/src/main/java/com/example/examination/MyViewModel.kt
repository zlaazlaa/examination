package com.example.examination

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModel: ViewModel() {
    val textLiveData = MutableLiveData<String>()
}