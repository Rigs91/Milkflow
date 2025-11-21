package com.example.milkflow.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "baby_profiles")
data class BabyProfileEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val dateOfBirth: String? = null,
    val isActive: Boolean = false
)
