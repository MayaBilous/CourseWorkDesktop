package org.example.project

import android.app.Application
import org.example.project.data.db.SectionDataBaseProvider
import org.example.project.data.db.model.DbLogin
import org.example.project.data.db.model.DbSectionDetails
import org.example.project.data.db.model.DbSportSection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.example.project.data.db.InitData

class SectionApp : Application() {
    override fun onCreate() {
        super.onCreate()
        SectionDataBaseProvider.init(this)
        InitData(SectionDataBaseProvider.db)
    }
}
