package com.nfcmanager.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nfcmanager.data.model.NfcCardEntity

@Database(
    entities = [NfcCardEntity::class],
    version = 1,
    exportSchema = false
)
abstract class NFCDatabase : RoomDatabase() {
    abstract fun nfcCardDao(): NfcCardDao
}
