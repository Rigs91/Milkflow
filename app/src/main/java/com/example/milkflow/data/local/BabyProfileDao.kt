package com.example.milkflow.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface BabyProfileDao {
    @Query("SELECT * FROM baby_profiles WHERE isActive = 1 LIMIT 1")
    fun getActiveBaby(): Flow<BabyProfileEntity?>

    @Query("SELECT * FROM baby_profiles WHERE isActive = 1 LIMIT 1")
    suspend fun getActiveBabyOnce(): BabyProfileEntity?

    @Query("SELECT * FROM baby_profiles")
    fun getAllBabies(): Flow<List<BabyProfileEntity>>

    @Insert
    suspend fun insertBaby(profile: BabyProfileEntity): Long

    @Query("UPDATE baby_profiles SET isActive = 0")
    suspend fun clearActive()

    @Query("UPDATE baby_profiles SET isActive = 1 WHERE id = :babyId")
    suspend fun setActive(babyId: Long)

    @Query("SELECT COUNT(*) FROM baby_profiles")
    suspend fun countBabies(): Int

    @Query("SELECT * FROM baby_profiles LIMIT 1")
    suspend fun firstBaby(): BabyProfileEntity?

    @Transaction
    suspend fun insertAndSetActive(profile: BabyProfileEntity): Long {
        val id = insertBaby(profile)
        clearActive()
        setActive(id)
        return id
    }
}
