package com.example.beerapp.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.beerapp.data.model.RemoteKey

@Dao
interface RemoteKeyDAO {

    @Query("SELECT * FROM RemoteKey WHERE id =:id")
    suspend fun getRemoteKeys(id: Long): RemoteKey

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAllRemoteKeys(remoteKeys: List<RemoteKey>)

    @Query("DELETE FROM RemoteKey")
    suspend fun deleteAllRemoteKeys()

}