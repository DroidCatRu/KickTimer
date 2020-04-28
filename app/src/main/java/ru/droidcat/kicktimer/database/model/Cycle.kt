package ru.droidcat.kicktimer.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
        tableName = "cycles_table",
        foreignKeys = [
        ForeignKey(
                entity = Session::class,
                parentColumns = ["session_id"],
                childColumns = ["session_id"],
                onDelete = CASCADE)
        ]
)
data class Cycle(
        @PrimaryKey
        @ColumnInfo(name = "cycle_id") val cycle_id: String,
        @ColumnInfo(name = "cycle_work_time") val cycle_work_time: Int,
        @ColumnInfo(name = "cycle_rest_time") val cycle_rest_time: Int,
        @ColumnInfo(name = "session_id") val session_id: String
)