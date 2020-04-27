package ru.droidcat.kicktimer

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
        entities = [Project::class, Task::class, Session::class, Cycle::class],
        version = 1,
        exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun projectDao(): ProjectDAO
    abstract fun taskDao(): TaskDAO
    abstract fun sessionDao(): SessionDAO
    abstract fun cycleDao(): CycleDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(
                context: Context,
                scope: CoroutineScope
        ): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "kicktimer-db"
                )
                        .addCallback(AppDatabaseCallback(scope))
                        .build()

                INSTANCE = instance
                instance
            }
        }

        private class AppDatabaseCallback(
                private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        Log.d("Database", "Opened")
                    }
                }
            }
        }
    }
}