package ru.droidcat.kicktimer.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
        tableName = "sessions_table",
        foreignKeys = [
        ForeignKey(
                entity = Task::class,
                parentColumns = ["task_id"],
                childColumns = ["task_id"],
                onDelete = CASCADE)
        ]
)
data class Session(
        @PrimaryKey
        @ColumnInfo(name = "session_id") val session_id: String,
        @ColumnInfo(name = "session_name") val session_name: String,
        @ColumnInfo(name = "task_id") val task_id: String
)