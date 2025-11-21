package com.example.milkflow.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "feed_logs",
    foreignKeys = [
        ForeignKey(
            entity = BabyProfileEntity::class,
            parentColumns = ["id"],
            childColumns = ["babyId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["babyId"])]
)
data class FeedLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val babyId: Long,
    val type: String,
    val breastSide: String?,
    val startTime: Long,
    val endTime: Long?,
    val durationMinutes: Int,
    val amountMl: Int?,
    val notes: String?
)
