package com.lucasxvirtual.financasmercado.di

import android.app.Application
import androidx.room.Room
import com.lucasxvirtual.financasmercado.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    @Provides
    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(
            application.applicationContext,
            AppDatabase::class.java, AppDatabase.DATA_BASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}
