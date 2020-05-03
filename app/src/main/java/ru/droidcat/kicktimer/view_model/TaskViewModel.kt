package ru.droidcat.kicktimer.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.droidcat.kicktimer.database.AppDatabase
import ru.droidcat.kicktimer.database.TasksRepository
import ru.droidcat.kicktimer.database.model.Task
import java.text.SimpleDateFormat
import java.util.*

class TaskViewModel(application: Application): AndroidViewModel(application) {

    val taskDAO = AppDatabase.getDatabase(application, viewModelScope).taskDao()
    private lateinit var repository: TasksRepository
    lateinit var projectTasks: LiveData<List<Task>>
    private lateinit var projectId: String

    fun setProjectId(projectId: String) {
        repository = TasksRepository(taskDAO, projectId)
        projectTasks = repository.projectTasks
        this.projectId = projectId
    }

    fun insert() = viewModelScope.launch {
        val sdf = SimpleDateFormat("dd/M/yyyy HH:mm:ss:SSS", Locale.ENGLISH)
        val taskId = sdf.format(Date())
        val task = Task(taskId, "Task name", false, false, projectId)
        repository.insertTask(task)
    }

    fun delete(id: String) = viewModelScope.launch {
        repository.deleteTask(id)
    }

    fun size(): Int? {
        return projectTasks.value?.size
    }

    fun setIsDone(taskId: String, isDone: Boolean) = viewModelScope.launch {
        repository.updateTaskIsDone(taskId, isDone)
    }

    fun setIsFav(taskId: String, isFav: Boolean) = viewModelScope.launch {
        repository.updateTaskIsFav(taskId, isFav)
    }
}