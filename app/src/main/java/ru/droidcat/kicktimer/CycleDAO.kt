package ru.droidcat.kicktimer

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.droidcat.kicktimer.Cycle

@Dao
interface CycleDAO {
    @Query("SELECT * FROM cycles_table ORDER BY cycle_id ASC")
    fun getAllCycles(): LiveData<List<Cycle>>

    @Query("SELECT * FROM cycles_table WHERE session_id = :session_id ORDER BY cycle_id DESC")
    fun getSessionCycles(session_id: String): LiveData<List<Cycle>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(cycle: Cycle)

    @Query("DELETE FROM cycles_table")
    suspend fun deleteAllCycles()

    @Query("DELETE FROM cycles_table WHERE cycle_id = :cycle_id")
    suspend fun deleteCycle(cycle_id: String)

    @Query("DELETE FROM cycles_table WHERE session_id = :session_id")
    suspend fun deleteAllSessionCycles(session_id: String)

    @Query("UPDATE cycles_table SET cycle_work_time = :cycle_work_time WHERE cycle_id = :cycle_id")
    suspend fun setCycleWorkTime(cycle_id: String, cycle_work_time: Int)

    @Query("UPDATE cycles_table SET cycle_rest_time = :cycle_rest_time WHERE cycle_id = :cycle_id")
    suspend fun setCycleRestTime(cycle_id: String, cycle_rest_time: Int)

    @Query("SELECT count(cycle_id) AS cycles_count FROM cycles_table WHERE session_id = :session_id")
    fun getCyclesCount(session_id: String): LiveData<Int>

    @Query("SELECT SUM(cycle_work_time) AS session_work_time FROM cycles_table WHERE session_id = :session_id")
    fun getSessionWorkTime(session_id: String): LiveData<Int>

    @Query("SELECT SUM(cycle_rest_time) AS session_rest_time FROM cycles_table WHERE session_id = :session_id")
    fun getSessionRestTime(session_id: String): LiveData<Int>

}