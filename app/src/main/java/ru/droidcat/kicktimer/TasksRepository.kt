package ru.droidcat.kicktimer

import androidx.lifecycle.LiveData
import ru.droidcat.kicktimer.Task
import ru.droidcat.kicktimer.TaskDAO

class TasksRepository(
        private val taskDAO: TaskDAO,
        project_id: String) {

    val projectTasks: LiveData<List<Task>> = taskDAO.getProjectTasks(project_id)

    suspend fun insertTask(task: Task) {
        taskDAO.insert(task)
    }

    suspend fun deleteTask(task_id: String) {
        taskDAO.deleteTask(task_id)
    }

    suspend fun updateTaskName(task_id: String, task_name: String) {
        taskDAO.updateTaskName(task_id, task_name)
    }

    suspend fun updateTaskIsDone(task_id: String, task_is_done: Boolean) {
        taskDAO.updateTaskIsDone(task_id, task_is_done)
    }

    suspend fun updateTaskIsFav(task_id: String, task_is_fav: Boolean) {
        taskDAO.updateTaskIsFav(task_id, task_is_fav)
    }

    suspend fun moveTaskToProject(task_id: String, project_id: String) {
        taskDAO.moveToProject(task_id, project_id)
    }
}