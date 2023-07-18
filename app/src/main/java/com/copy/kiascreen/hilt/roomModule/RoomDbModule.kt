package com.copy.kiascreen.hilt.roomModule

import android.content.Context
import androidx.room.Room
import com.copy.kiascreen.room.RoomDao
import com.copy.kiascreen.room.RoomRepository
import com.copy.kiascreen.room.RoomRepositoryImpl
import com.copy.kiascreen.room.SampleRoomDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomDbModule {
    @Provides
    @Singleton
    fun provideSampleDateBase(@ApplicationContext context : Context) =
        Room.databaseBuilder(
            context,
            SampleRoomDataBase::class.java,
            "sample_db"
        ).fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideUserDao(db : SampleRoomDataBase) = db.roomDao()

    @Singleton
    @Provides
    fun provideRepository(roomDao : RoomDao) :RoomRepository = RoomRepositoryImpl(roomDao)
}