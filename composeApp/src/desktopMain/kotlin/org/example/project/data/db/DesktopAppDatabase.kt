package org.example.project.data.db

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import jdk.internal.net.http.common.Log
import org.example.project.data.db.SectionDataBase.Companion.DB_NAME
import java.io.File

fun getDatabaseBuilder(): RoomDatabase.Builder<SectionDataBase> {
    val dbFile = File(System.getProperty("java.io.tmpdir"), DB_NAME)
    return Room.databaseBuilder<SectionDataBase>(
        name = dbFile.absolutePath,
    ).setDriver(BundledSQLiteDriver())
}
