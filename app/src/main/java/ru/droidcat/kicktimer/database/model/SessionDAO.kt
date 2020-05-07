package ru.droidcat.kicktimer.database.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.droidcat.kicktimer.database.model.Session
import ru.droidcat.kicktimer.view_model.ListSession

@Dao
interface SessionDAO {
    @Query("SELECT * FROM sessions_table ORDER BY session_id ASC")
    fun getAllSessions(): LiveData<List<Session>>

    @Query("SELECT s.session_id AS session_id, s.session_name AS session_name, s.task_id AS task_id, c.work_time AS work_time, c.rest_time AS rest_time  FROM sessions_table AS s JOIN (SELECT SUM(cycle_work_time) AS work_time, SUM(cycle_rest_time) AS rest_time, session_id FROM cycles_table GROUP BY session_id) AS c ON c.session_id = s.session_id WHERE s.task_id = :task_id ORDER BY s.session_id DESC")
    fun getTaskSessions(task_id: String): LiveData<List<ListSession>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(session: Session)

    @Query("DELETE FROM sessions_table")
    suspend fun deleteAllSessions()

    @Query("DELETE FROM sessions_table WHERE session_id = :session_id")
    suspend fun deleteSession(session_id: String)

    @Query("DELETE FROM sessions_table WHERE task_id = :task_id")
    suspend fun deleteAllTaskSessions(task_id: String)

    @Query("UPDATE sessions_table SET session_name = :session_name WHERE session_id = :session_id")
    suspend fun updateSessionName(session_id: String, session_name: String)

    @Query("SELECT count(session_id) AS sessions_count FROM sessions_table WHERE task_id = :task_id")
    fun getSessionsCount(task_id: String): LiveData<Int>

}