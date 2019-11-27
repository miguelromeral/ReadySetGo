package es.miguelromeral.readysetgo.ui.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ApplicationDatabaseDao {

    @Insert
    fun insert(start: Start)

    @Update
    fun update(start: Start)

    @Query("SELECT * from starts_table WHERE startId = :key")
    fun get(key: Long): Start?

    @Query("DELETE FROM starts_table")
    fun clear_starts()

    @Query("SELECT * FROM starts_table ORDER BY startId DESC")
    fun getAllStarts(): LiveData<List<Start>>

    @Query("SELECT MIN(time) AS min, * FROM starts_table ORDER BY waiting_time ASC LIMIT 1")
    fun getBestStart(): LiveData<Start>
}