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

class ProjectViewModel(application: Application) : AndroidViewModel(application) {

    val projectDAO = AppDatabase.getDatabase(application, viewModelScope).projectDao()
    private val repository: ProjectsRepository = ProjectsRepository(projectDAO)
    val allProjects: LiveData<List<Project>> = repository.allProjects

    fun insert() = viewModelScope.launch {
        val sdf = SimpleDateFormat("dd/M/yyyy HH:mm:ss:SSS", Locale.ENGLISH)
        val currentDate = sdf.format(Date())
        val pos = size()!!
        val project = Project(currentDate, "My test project", pos)
        repository.insertProject(project)
    }

    fun size(): Int? {
        return allProjects.value?.size
    }

    fun moveItem(from: Int, to: Int) = viewModelScope.launch {
        val id = getItemId(from)
        repository.moveProject(id!!, from, to)
        /*
        if(from < to) {
            for (project in projects!!) {
                val pos = project.project_pos
                if(pos in (from + 1) until to) {
                    repository.updateProjectPos(getItemId(pos)!!, pos-1)
                }
            }
        }
        else if(from > to) {
            for (project in projects!!) {
                val pos = project.project_pos
                if(pos in to until (from-1)) {
                    repository.updateProjectPos(getItemId(pos)!!, pos+1)
                }
            }
        }
        repository.updateProjectPos(getItemId(from)!!, to)
        */
    }

    private fun getItemId(pos: Int): String? {
        val projects: List<Project>? = repository.allProjects.value
        for(project in projects!!) {
            if(project.project_pos == pos) {
                return project.project_id
            }
        }
        return null
    }
}