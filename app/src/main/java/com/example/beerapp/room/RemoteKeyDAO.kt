package com.example.beerapp.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.beerapp.data.model.RemoteKey

@Dao
interface RemoteKeyDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(remoteKey: RemoteKey)

    @Query("DELETE FROM RemoteKey")
    suspend fun clearAll()

    @Query("SELECT * FROM RemoteKey WHERE id==:id")
    suspend fun getAllRemoteKeys(id: Long): RemoteKey

}