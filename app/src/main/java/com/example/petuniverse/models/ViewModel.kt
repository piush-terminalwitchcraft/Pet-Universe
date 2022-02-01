package com.example.petuniverse.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddViewModel: ViewModel(){
    private val _status = MutableLiveData<Status>(Status.INITIAL)
    val status: LiveData<Status> = _status
    fun setstatus(st: Status){
        _status.value = st
    }
    fun reset(){
        _status.value = Status.INITIAL
    }

    fun onCreatePostFragment(){
        _status.value = Status.START
    }
    fun getStatus(): Status?{
        return status.value
    }
}