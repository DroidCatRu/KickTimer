package ru.droidcat.kicktimer.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.droidcat.kicktimer.database.AppDatabase
import ru.droidcat.kicktimer.database.ProjectsRepository
import ru.droidcat.kicktimer.database.model.Project
import java.text.SimpleDateFormat
import java.util.*

class ProjectViewModel(application: Application): AndroidViewModel(application) {

    val projectDAO = AppDatabase.getDatabase(application, viewModelScope).projectDao()
    private val repository: ProjectsRepository = ProjectsRepository(projectDAO)
    val allProjects: LiveData<List<Project>> = repository.allProjects

    fun insert(name: String) = viewModelScope.launch {
        val sdf = SimpleDateFormat("dd/M/yyyy HH:mm:ss:SSS", Locale.ENGLISH)
        val currentDate = sdf.format(Date())
        val pos = size()!!
        val project = Project(currentDate, name, pos)
        repository.insertProject(project)
    }

    fun size(): Int? {
        return allProjects.value?.size
    }

    fun moveItem(from: Int, to: Int) = viewModelScope.launch {
        val id = getItemId(from)
        repository.moveProject(id!!, from, to)
    }

    fun getItemId(pos: Int): String? {
        val projects: List<Project>? = repository.allProjects.value
        for(project in projects!!) {
            if(project.project_pos == pos) {
                return project.project_id
            }
        }
        return null
    }
}