package com.gelato.picsum.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gelato.picsum.data.models.RemoteKey

@Dao
interface RemoteDao {
     @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertOrReplace(remoteKey: List<RemoteKey>)

    @Query("SELECT * FROM remote_keys WHERE id = :id")
    suspend fun remoteKeyByQuery(id: String): RemoteKey

    @Query("DELETE FROM remote_keys")
    suspend fun clearAll()
}