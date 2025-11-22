package com.example.milkflow.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FeedLogDao {
    @Insert
    suspend fun insertFeed(log: FeedLogEntity): Long

    @Update
    suspend fun updateFeed(log: FeedLogEntity)

    @Query(
        "SELECT * FROM feed_logs WHERE babyId = :babyId AND startTime BETWEEN :start AND :end ORDER BY startTime DESC"
    )
    fun feedsForDay(babyId: Long, start: Long, end: Long): Flow<List<FeedLogEntity>>

    @Query("SELECT * FROM feed_logs WHERE babyId = :babyId ORDER BY startTime DESC LIMIT 1")
    fun lastFeed(babyId: Long): Flow<FeedLogEntity?>

    @Query(
        "SELECT * FROM feed_logs WHERE babyId = :babyId AND type = 'BREAST' ORDER BY startTime DESC LIMIT 1"
    )
    fun lastBreastFeed(babyId: Long): Flow<FeedLogEntity?>

    @Query(
        "SELECT COUNT(*) FROM feed_logs WHERE babyId = :babyId AND startTime BETWEEN :start AND :end"
    )
    fun feedCountForDay(babyId: Long, start: Long, end: Long): Flow<Int>

    @Query(
        "SELECT COALESCE(SUM(amountMl),0) FROM feed_logs WHERE babyId = :babyId AND type IN ('BOTTLE','PUMP') AND startTime BETWEEN :start AND :end"
    )
    fun totalVolumeForDay(babyId: Long, start: Long, end: Long): Flow<Int>
}
