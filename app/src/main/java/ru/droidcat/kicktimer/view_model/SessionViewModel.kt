package ru.droidcat.kicktimer.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.droidcat.kicktimer.database.AppDatabase
import ru.droidcat.kicktimer.database.SessionsRepository
import ru.droidcat.kicktimer.database.model.Session
import java.text.SimpleDateFormat
import java.util.*

class SessionViewModel(application: Application): AndroidViewModel(application) {

    val sessionDAO = AppDatabase.getDatabase(application, viewModelScope).sessionDao()
    val cycleDAO = AppDatabase.getDatabase(application, viewModelScope).cycleDao()
    val taskDAO = AppDatabase.getDatabase(application, viewModelScope).taskDao()
    private lateinit var repository: SessionsRepository
    lateinit var taskSessions: LiveData<List<ListSession>>
    private lateinit var taskId: String

    fun setTaskId(taskId: String) {
        repository = SessionsRepository(sessionDAO, cycleDAO, taskDAO, taskId)
        taskSessions = repository.taskSessions
        this.taskId = taskId
    }

    fun insert(sessionId: String) = viewModelScope.launch {
        val sdfName = SimpleDateFormat("HH:mm dd.MM.yyyy", Locale.ENGLISH)
        val sessionName = sdfName.format(Date())
        val session = Session(sessionId, sessionName, taskId)
        repository.insertSession(session)
    }

    fun deleteTask() = viewModelScope.launch {
        repository.deleteTask()
    }

    fun renameTask(name: String) = viewModelScope.launch {
        repository.renameTask(name)
    }

    fun getWorkTime(): LiveData<Int> = repository.getSessionWorkTime(taskId)
    fun getRestTime(): LiveData<Int> = repository.getSessionRestTime(taskId)
}