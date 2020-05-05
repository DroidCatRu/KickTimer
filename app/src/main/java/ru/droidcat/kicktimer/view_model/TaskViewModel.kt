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
    val projectDAO = AppDatabase.getDatabase(application, viewModelScope).projectDao()
    private lateinit var repository: TasksRepository
    lateinit var projectTasks: LiveData<List<Task>>
    private lateinit var projectId: String

    fun setProjectId(projectId: String) {
        repository = TasksRepository(taskDAO, projectDAO, projectId)
        projectTasks = repository.projectTasks
        this.projectId = projectId
    }

    fun insert(name: String) = viewModelScope.launch {
        val sdf = SimpleDateFormat("dd/M/yyyy HH:mm:ss:SSS", Locale.ENGLISH)
        val taskId = sdf.format(Date())
        val task = Task(taskId, name, false, false, projectId)
        repository.insertTask(task)
    }

    fun deleteTask(id: String) = viewModelScope.launch {
        repository.deleteTask(id)
    }

    fun deleteProject(id: String) = viewModelScope.launch {
        repository.deleteProject(id)
    }

    fun renameProject(id: String, name: String) = viewModelScope.launch {
        repository.updateProjectName(id, name)
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