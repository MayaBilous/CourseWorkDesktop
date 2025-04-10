package org.example.project.data.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import org.example.project.data.db.SectionDataBase.Companion.DB_NAME

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<SectionDataBase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath(DB_NAME)
    return Room.databaseBuilder<SectionDataBase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}
