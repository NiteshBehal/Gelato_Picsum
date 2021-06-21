package com.gelato.picsum.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gelato.picsum.data.models.ImageData
import kotlinx.coroutines.flow.Flow

@Dao
 interface ImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<ImageData>)

    @Query("SELECT * FROM images order by _id")
    fun pagingSource(): PagingSource<Int, ImageData>

    @Query("DELETE FROM images")
    suspend fun clearAll()

   @Query("SELECT * FROM images order by _id")
   fun getAllImages(): Flow<List<ImageData>>
}