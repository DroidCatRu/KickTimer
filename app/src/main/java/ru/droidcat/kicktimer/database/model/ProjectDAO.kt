package ru.droidcat.kicktimer

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.droidcat.kicktimer.Project

@Dao
interface ProjectDAO {
    @Query("SELECT * FROM projects_table ORDER BY project_pos ASC")
    fun getAllProjects(): LiveData<List<Project>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(project: Project)

    @Query("DELETE FROM projects_table")
    suspend fun deleteAllProjects()

    @Query("DELETE FROM projects_table WHERE project_id = :project_id")
    suspend fun deleteProject(project_id: String)

    @Query("UPDATE projects_table SET project_name = :project_name WHERE project_id = :project_id")
    suspend fun updateProjectName(project_id: String, project_name: String)

    @Query("UPDATE projects_table SET project_pos = :project_pos WHERE project_id = :project_id")
    suspend fun updateProjectPos(project_id: String, project_pos: Int)

    @Query("SELECT COUNT(project_id) FROM projects_table")
    fun getProjectsCount(): LiveData<Int>
}