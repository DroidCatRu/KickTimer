package ru.droidcat.kicktimer.database.model

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.droidcat.kicktimer.database.model.Project

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

    @Query("UPDATE projects_table SET project_pos = project_pos-1 WHERE project_id IN (SELECT project_id FROM projects_table t WHERE t.project_pos > :from AND t.project_pos <= :to)")
    suspend fun fromTo(from: Int, to: Int)

    @Query("UPDATE projects_table SET project_pos = project_pos+1 WHERE project_id IN (SELECT project_id FROM projects_table t WHERE t.project_pos >= :to AND t.project_pos < :from)")
    suspend fun toFrom(from: Int, to: Int)
}