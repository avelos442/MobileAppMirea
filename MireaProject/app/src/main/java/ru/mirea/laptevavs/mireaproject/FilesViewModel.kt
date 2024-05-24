package ru.mirea.laptevavs.mireaproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FilesViewModel : ViewModel() {
    val path = MutableLiveData<String>()
    //val publicPath: LiveData<String> = path
}