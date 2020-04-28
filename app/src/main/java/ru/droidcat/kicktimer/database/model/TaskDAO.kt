package ru.droidcat.kicktimer.database.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.droidcat.kicktimer.database.model.Task

@Dao
interface TaskDAO {
    @Query("SELECT * FROM tasks_table ORDER BY task_id ASC")
    fun getAllTasks(): LiveData<List<Task>>

    @Query("SELECT * FROM tasks_table WHERE project_id = :project_id ORDER BY task_is_done, task_is_fav, task_id ASC")
    fun getProjectTasks(project_id: String): LiveData<List<Task>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(task: Task)

    @Query("DELETE FROM tasks_table")
    suspend fun deleteAllTasks()

    @Query("DELETE FROM tasks_table WHERE task_id = :task_id")
    suspend fun deleteTask(task_id: String)

    @Query("DELETE FROM tasks_table WHERE project_id = :project_id")
    suspend fun deleteAllProjectTasks(project_id: String)

    @Query("UPDATE tasks_table SET task_name = :task_name WHERE task_id = :task_id")
    suspend fun updateTaskName(task_id: String, task_name: String)

    @Query("UPDATE tasks_table SET task_is_done = :task_is_done WHERE task_id = :task_id")
    suspend fun updateTaskIsDone(task_id: String, task_is_done: Boolean)

    @Query("UPDATE tasks_table SET task_is_fav = :task_is_fav WHERE task_id = :task_id")
    suspend fun updateTaskIsFav(task_id: String, task_is_fav: Boolean)

    @Query("UPDATE tasks_table SET project_id = :project_id WHERE task_id = :task_id")
    suspend fun moveToProject(task_id: String, project_id: String)

    @Query("SELECT count(task_id) AS tasks_count FROM tasks_table WHERE project_id = :project_id")
    fun getTasksCount(project_id: String): LiveData<Int>
}