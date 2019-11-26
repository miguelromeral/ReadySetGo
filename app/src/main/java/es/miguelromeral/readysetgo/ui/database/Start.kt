package es.miguelromeral.readysetgo.ui.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "starts_table")
data class Start(

    @PrimaryKey(autoGenerate = true)
    var startId: Long = 0L,

    @ColumnInfo(name = "date")
    val date: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "time")
    val time: Long = -1L,

    @ColumnInfo(name = "waiting_time")
    val waitingTime: Long = 0L
)