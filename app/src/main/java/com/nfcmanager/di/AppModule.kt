package com.nfcmanager.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.nfcmanager.data.db.NFCDatabase
import com.nfcmanager.data.db.NfcCardDao
import com.nfcmanager.data.repository.NfcCardRepositoryImpl
import com.nfcmanager.domain.repository.NfcCardRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): NFCDatabase =
        Room.databaseBuilder(context, NFCDatabase::class.java, "nfc_manager.db")
            .addMigrations(NFCDatabase.MIGRATION_1_2)
            .build()

    @Provides
    @Singleton
    fun provideDao(db: NFCDatabase): NfcCardDao = db.nfcCardDao()

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideRepository(dao: NfcCardDao, gson: Gson): NfcCardRepository =
        NfcCardRepositoryImpl(dao, gson)
}
