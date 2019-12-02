package es.miguelromeral.readysetgo.ui.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import es.miguelromeral.readysetgo.MyApplication
import es.miguelromeral.readysetgo.R

@Database(entities = [Start::class], version = 3, exportSchema = false)
abstract class ReadySetGoDatabase : RoomDatabase() {
    abstract val database : ApplicationDatabaseDao

    companion object{
        @Volatile
        private var INSTANCE: ReadySetGoDatabase ? = null

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE starts_table ADD COLUMN game_mode INTEGER NOT NULL DEFAULT 0")
                database.execSQL("UPDATE starts_table SET game_mode = 0 WHERE game_mode IS NULL")
            }
        }

        fun getInstance(context: Context): ReadySetGoDatabase  {
            synchronized(this){
                var instance = INSTANCE
                if(instance == null){

                    instance = Room.databaseBuilder(context.applicationContext,
                                        ReadySetGoDatabase::class.java,"ready_set_go_database")
                        .addMigrations(MIGRATION_2_3)
                        .fallbackToDestructiveMigration()
                        .build()
                }
                return instance
            }
        }

        fun getGameModeFromString(mode: String?): Int {
            mode?.let {
                return when (mode) {
                    MyApplication.allResources.getString(R.string.preference_game_mode_two) -> 1
                    else -> 0
                }
            }
            return 0
        }

        fun getGameModeFromIntFormatted(mode: Int) = when(mode){
            1 -> MyApplication.allResources.getString(R.string.preference_string_game_mode_two)
            else -> MyApplication.allResources.getString(R.string.preference_string_game_mode_one)
        }

    }
}