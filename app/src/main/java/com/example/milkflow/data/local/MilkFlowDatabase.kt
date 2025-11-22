package com.example.milkflow.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [BabyProfileEntity::class, FeedLogEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MilkFlowDatabase : RoomDatabase() {
    abstract fun babyProfileDao(): BabyProfileDao
    abstract fun feedLogDao(): FeedLogDao

    companion object {
        @Volatile
        private var INSTANCE: MilkFlowDatabase? = null

        fun getDatabase(context: Context): MilkFlowDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    MilkFlowDatabase::class.java,
                    "milkflow.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
