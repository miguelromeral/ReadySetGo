package es.miguelromeral.readysetgo.ui.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Start::class], version = 1, exportSchema = false)
abstract class ReadySetGoDatabase : RoomDatabase() {
    abstract val database : ApplicationDatabaseDao

    companion object{
        @Volatile
        private var INSTANCE: ReadySetGoDatabase ? = null

        fun getInstance(context: Context): ReadySetGoDatabase  {
            synchronized(this){
                var instance = INSTANCE
                if(instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ReadySetGoDatabase::class.java,
                        "ready_set_go_database").fallbackToDestructiveMigration().build()
                }
                return instance
            }
        }
    }
}