package ru.droidcat.kicktimer.database

import androidx.lifecycle.LiveData
import ru.droidcat.kicktimer.database.model.CycleDAO
import ru.droidcat.kicktimer.database.model.Session
import ru.droidcat.kicktimer.database.model.SessionDAO
import ru.droidcat.kicktimer.database.model.TaskDAO
import ru.droidcat.kicktimer.view_model.ListSession

class SessionsRepository(
        private val sessionDAO: SessionDAO,
        private val cycleDAO: CycleDAO,
        private val taskDAO: TaskDAO,
        private val task_id: String) {

    val taskSessions: LiveData<List<ListSession>> = sessionDAO.getTaskSessions(task_id)

    fun getSessionWorkTime(session_id: String): LiveData<Int> {
        return cycleDAO.getSessionWorkTime(session_id)
    }

    fun getSessionRestTime(session_id: String): LiveData<Int> {
        return cycleDAO.getSessionRestTime(session_id)
    }

    suspend fun insertSession(session: Session) {
        sessionDAO.insert(session)
    }

    suspend fun deleteSession(session_id: String) {
        sessionDAO.deleteSession(session_id)
    }

    suspend fun updateSessionName(session_id: String, session_name: String) {
        sessionDAO.updateSessionName(session_id, session_name)
    }

    suspend fun deleteTask() {
        taskDAO.deleteTask(task_id)
    }

    suspend fun renameTask(name: String) {
        taskDAO.updateTaskName(task_id, name)
    }
}