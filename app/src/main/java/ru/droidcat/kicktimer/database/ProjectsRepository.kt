package ru.droidcat.kicktimer.database

import androidx.lifecycle.LiveData
import ru.droidcat.kicktimer.database.model.Project
import ru.droidcat.kicktimer.database.model.ProjectDAO

class ProjectsRepository(private val projectDAO: ProjectDAO) {

    val allProjects: LiveData<List<Project>> = projectDAO.getAllProjects()

    suspend fun insertProject(project: Project) {
        projectDAO.insert(project)
    }

    suspend fun deleteProject(project_id: String) {
        projectDAO.deleteProject(project_id)
    }

    suspend fun updateProjectName(project_id: String, project_name: String) {
        projectDAO.updateProjectName(project_id, project_name)
    }

    suspend fun updateProjectPos(project_id: String, project_pos: Int) {
        projectDAO.updateProjectPos(project_id, project_pos)
    }
}