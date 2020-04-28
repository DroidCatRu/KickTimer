package ru.droidcat.kicktimer.database

import androidx.lifecycle.LiveData
import ru.droidcat.kicktimer.database.model.CycleDAO
import ru.droidcat.kicktimer.database.model.Cycle

class CyclesRepository(private val cycleDAO: CycleDAO, session_id: String) {

    val sessionCycles: LiveData<List<Cycle>> = cycleDAO.getSessionCycles(session_id)

    suspend fun insertCycle(cycle: Cycle) {
        cycleDAO.insert(cycle)
    }

    suspend fun deleteCycle(cycle_id: String) {
        cycleDAO.deleteCycle(cycle_id)
    }

    suspend fun setCycleWorkTime(cycle_id: String, work_time_sec: Int) {
        cycleDAO.setCycleWorkTime(cycle_id, work_time_sec)
    }

    suspend fun setCycleRestTime(cycle_id: String, rest_time_sec: Int) {
        cycleDAO.setCycleRestTime(cycle_id, rest_time_sec)
    }
}