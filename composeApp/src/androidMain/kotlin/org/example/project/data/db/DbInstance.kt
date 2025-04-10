package org.example.project.data.db

import android.content.Context

object DbInstance {

    lateinit var appDatabase: SectionDataBase

    fun init(context: Context) {
        appDatabase = getDatabaseBuilder(context).build()
    }
}
