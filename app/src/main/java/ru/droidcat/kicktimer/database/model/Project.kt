package ru.droidcat.kicktimer.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects_table")
data class Project(
        @PrimaryKey
        @ColumnInfo(name = "project_id") val project_id: String,
        @ColumnInfo(name = "project_name") val project_name: String,
        @ColumnInfo(name = "project_pos") val project_pos: Int)