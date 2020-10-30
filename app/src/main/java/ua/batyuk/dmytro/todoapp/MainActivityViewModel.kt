package ua.batyuk.dmytro.todoapp

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private const val REQ_TAG = "myTag"

class MainActivityViewModel(app: Application) : AndroidViewModel(app) {
    private val queue = Volley.newRequestQueue(app)

    val taskTitle = MutableLiveData<String>()
    val taskCanBeAdded: MutableLiveData<Boolean> by lazy {
        MediatorLiveData<Boolean>().apply {
            addSource(taskTitle) {
                value = it.isNotEmpty()
            }
        }
    }

    val data: MutableLiveData<Array<Task>> by lazy {
        MutableLiveData<Array<Task>>().also {
            loadData()
        }
    }


    fun onAddClicked() {
        val newList = arrayOf(Task(0, taskTitle.value!!, false), *(data.value ?: emptyArray()))
        taskTitle.value = ""
        data.value = newList
    }

    fun onCompletedChanged(position: Int, isCompleted: Boolean) {
        data.value!![position].completed = isCompleted
        data.postValue(data.value)
    }

    override fun onCleared() {
        super.onCleared()
        queue.cancelAll(REQ_TAG)
    }

    private fun loadData() {
        val url = "http://jsonplaceholder.typicode.com/todos?userId=1"
        val request = StringRequest(Request.Method.GET, url, { response ->
            val arrayType = object : TypeToken<Array<Task>>() {}.type
            val array: Array<Task> = Gson().fromJson(response, arrayType)
            data.postValue(array)
        }, { err ->
            //For testing app is OK
            Toast.makeText(getApplication(), "Failed: $err", Toast.LENGTH_LONG).show()
        })
            .apply {
                tag = REQ_TAG
            }

        queue.add(request)
    }
}


data class Task(
    val id: Long,
    val title: String,
    var completed: Boolean
)
