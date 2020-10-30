package ua.batyuk.dmytro.todoapp

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {
    val taskTitle = MutableLiveData<String>()
    val taskCanBeAdded: MutableLiveData<Boolean> by lazy {
        MediatorLiveData<Boolean>().apply {
            addSource(taskTitle) {
                value = it.isNotEmpty()
            }
        }
    }
}