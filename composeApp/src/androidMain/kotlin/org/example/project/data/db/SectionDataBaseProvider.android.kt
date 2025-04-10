package org.example.project.data.db

import android.content.Context
import androidx.room.Room
import org.example.project.data.db.SectionDataBase.Companion.DB_NAME

actual object SectionDataBaseProvider {

    actual val db: SectionDataBase
        get() = _db

    private lateinit var _db: SectionDataBase

    fun init(applicationContext: Any?) {
        if (applicationContext is Context) {
            _db = Room.databaseBuilder(
                context = applicationContext,
                klass = SectionDataBase::class.java,
                name = DB_NAME
            ).fallbackToDestructiveMigration(false)
                .build()
        }
    }
}