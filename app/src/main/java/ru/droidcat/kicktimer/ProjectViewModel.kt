package ru.droidcat.kicktimer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ProjectViewModel(application: Application) : AndroidViewModel(application) {

    val projectDAO = AppDatabase.getDatabase(application, viewModelScope).projectDao()
    private val repository: ProjectsRepository = ProjectsRepository(projectDAO)
    val allProjects: LiveData<List<Project>> = repository.allProjects

    fun insert() = viewModelScope.launch {
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss", Locale.ENGLISH)
        val currentDate = sdf.format(Date())
        val pos = size()!!
        val project = Project(currentDate, "My test project", pos)
        repository.insertProject(project)
    }

    fun size(): Int? {
        return allProjects.value?.size
    }
}