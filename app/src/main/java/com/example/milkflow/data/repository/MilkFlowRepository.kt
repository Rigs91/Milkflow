package com.example.milkflow.data.repository

import com.example.milkflow.data.local.BabyProfileDao
import com.example.milkflow.data.local.BabyProfileEntity
import com.example.milkflow.data.local.FeedLogDao
import com.example.milkflow.data.local.FeedLogEntity
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.TimeUnit

class MilkFlowRepository(
    private val babyProfileDao: BabyProfileDao,
    private val feedLogDao: FeedLogDao
) {
    val activeBaby: Flow<BabyProfileEntity?> = babyProfileDao.getActiveBaby()
    val allBabies: Flow<List<BabyProfileEntity>> = babyProfileDao.getAllBabies()

    suspend fun ensureDefaultBaby(): Long {
        val active = babyProfileDao.getActiveBabyOnce()
        if (active != null) return active.id
        val count = babyProfileDao.countBabies()
        return if (count == 0) {
            babyProfileDao.insertAndSetActive(BabyProfileEntity(name = "Baby", isActive = true))
        } else {
            val first = babyProfileDao.firstBaby()
            if (first != null) {
                babyProfileDao.setActive(first.id)
                first.id
            } else {
                babyProfileDao.insertAndSetActive(BabyProfileEntity(name = "Baby", isActive = true))
            }
        }
    }

    suspend fun addBaby(name: String, dateOfBirth: String?) {
        val id = babyProfileDao.insertBaby(
            BabyProfileEntity(name = name, dateOfBirth = dateOfBirth, isActive = false)
        )
        babyProfileDao.clearActive()
        babyProfileDao.setActive(id)
    }

    suspend fun setActiveBaby(id: Long) {
        babyProfileDao.clearActive()
        babyProfileDao.setActive(id)
    }

    suspend fun logBreastFeed(
        babyId: Long,
        breastSide: String?,
        startTime: Long,
        endTime: Long?,
        durationMinutes: Int,
        notes: String?
    ) {
        feedLogDao.insertFeed(
            FeedLogEntity(
                babyId = babyId,
                type = "BREAST",
                breastSide = breastSide,
                startTime = startTime,
                endTime = endTime,
                durationMinutes = durationMinutes,
                amountMl = null,
                notes = notes
            )
        )
    }

    suspend fun logBottleFeed(
        babyId: Long,
        time: Long,
        amountMl: Int?,
        notes: String?
    ) {
        feedLogDao.insertFeed(
            FeedLogEntity(
                babyId = babyId,
                type = "BOTTLE",
                breastSide = null,
                startTime = time,
                endTime = time,
                durationMinutes = 0,
                amountMl = amountMl,
                notes = notes
            )
        )
    }

    suspend fun logPump(
        babyId: Long,
        startTime: Long,
        durationMinutes: Int,
        amountMl: Int?,
        breastSide: String?,
        notes: String?
    ) {
        feedLogDao.insertFeed(
            FeedLogEntity(
                babyId = babyId,
                type = "PUMP",
                breastSide = breastSide,
                startTime = startTime,
                endTime = startTime + TimeUnit.MINUTES.toMillis(durationMinutes.toLong()),
                durationMinutes = durationMinutes,
                amountMl = amountMl,
                notes = notes
            )
        )
    }

    fun feedsForDay(babyId: Long, start: Long, end: Long): Flow<List<FeedLogEntity>> =
        feedLogDao.feedsForDay(babyId, start, end)

    fun lastFeed(babyId: Long): Flow<FeedLogEntity?> = feedLogDao.lastFeed(babyId)

    fun lastBreastFeed(babyId: Long): Flow<FeedLogEntity?> = feedLogDao.lastBreastFeed(babyId)

    fun feedCountForDay(babyId: Long, start: Long, end: Long): Flow<Int> =
        feedLogDao.feedCountForDay(babyId, start, end)

    fun totalVolumeForDay(babyId: Long, start: Long, end: Long): Flow<Int> =
        feedLogDao.totalVolumeForDay(babyId, start, end)
}
