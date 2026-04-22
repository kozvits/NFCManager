package com.nfcmanager.data.db

import androidx.room.Database
import androidx.room.migration.Migration
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.nfcmanager.data.model.NfcCardEntity

@Database(
    entities = [NfcCardEntity::class],
    version = 2,
    exportSchema = false
)
abstract class NFCDatabase : RoomDatabase() {
    abstract fun nfcCardDao(): NfcCardDao

    companion object {
        // Миграция v1 -> v2: добавили поле uidLsb
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE nfc_cards ADD COLUMN uidLsb TEXT NOT NULL DEFAULT ''")
            }
        }
    }
}
