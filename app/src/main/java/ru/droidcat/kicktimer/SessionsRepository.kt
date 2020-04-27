package ru.droidcat.kicktimer

import androidx.lifecycle.LiveData
import ru.droidcat.kicktimer.CycleDAO
import ru.droidcat.kicktimer.Session
import ru.droidcat.kicktimer.SessionDAO

class SessionsRepository(
        private val sessionDAO: SessionDAO,
        private val cycleDAO: CycleDAO,
        task_id: String) {

    val taskSessions: LiveData<List<Session>> = sessionDAO.getTaskSessions(task_id)

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
}