package io.aparok.app.persistence.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import io.aparok.app.persistence.common.Config
import io.aparok.app.persistence.common.Provider
import io.aparok.app.persistence.dao.AccountDao
import io.aparok.app.persistence.model.AccountTable

@Database(
    entities = [AccountTable::class],
    exportSchema = false,
    version = 1
)
abstract class AnytypeDatabase : RoomDatabase() {

    abstract fun accountDao(): AccountDao

    companion object : Provider<Context, AnytypeDatabase>() {
        override fun create(param: Context) = databaseBuilder(
            param.applicationContext,
            AnytypeDatabase::class.java,
            Config.DATABASE_NAME
        ).build()
    }
}