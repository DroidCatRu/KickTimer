package ru.droidcat.kicktimer

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import ru.droidcat.kicktimer.Project

@Entity(
        tableName = "tasks_table",
        foreignKeys = [
        ForeignKey(
                entity = Project::class,
                parentColumns = ["project_id"],
                childColumns = ["project_id"],
                onDelete = CASCADE)
        ]
)
data class Task(
        @PrimaryKey
        @ColumnInfo(name = "task_id") val task_id: String,
        @ColumnInfo(name = "task_name") val task_name: String,
        @ColumnInfo(name = "task_is_done") val task_is_done: Boolean,
        @ColumnInfo(name = "task_is_fav") val task_is_fav: Boolean,
        @ColumnInfo(name = "project_id") val project_id: String)