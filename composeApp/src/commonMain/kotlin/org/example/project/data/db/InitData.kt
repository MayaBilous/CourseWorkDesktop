package org.example.project.data.db

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.example.project.data.db.model.DbLogin
import org.example.project.data.db.model.DbSectionDetails
import org.example.project.data.db.model.DbSportSection

object InitData {

    operator fun invoke(db: SectionDataBase) {
        val coroutineScope = CoroutineScope(SupervisorJob())
        coroutineScope.launch {
            if (db.LoginDao().getAll().isEmpty()) {
                db.LoginDao().insert(
                    DbLogin(id = 0, userName = "admin", password = "123", isAdmin = true),
                    DbLogin(id = 1, userName = "user", password = "321", isAdmin = false),
                )
            }
            if (db.SportSectionDao().getSectionsWithDetails().isEmpty()) {
                val sportSection1 = DbSportSection(id = 0, sectionName = "Футбол")
                val sportSection2 = DbSportSection(id = 1, sectionName = "Баскетбол")
                val sportSection3 = DbSportSection(id = 2, sectionName = "Волейбол")
                val sportSection4 = DbSportSection(id = 3, sectionName = "Теніс")

                db.SportSectionDao()
                    .addSportSection(sportSection1, sportSection2, sportSection3, sportSection4)

                val sectionDetailsList = listOf(
                    DbSectionDetails(
                        id = 0,
                        sectionId = 0,
                        address = "вул. Спортивна, 10",
                        workingDays = "Пн-Пт, 9:00-18:00",
                        phoneNumber = "0501234567",
                        price = 500,
                        isSelected = false
                    ),
                    DbSectionDetails(
                        id = 1,
                        sectionId = 1,
                        address = "вул. Баскетбольна, 15",
                        workingDays = "Пн-Пт, 10:00-20:00",
                        phoneNumber = "0509876543",
                        price = 400,
                        isSelected = false
                    ),
                    DbSectionDetails(
                        id = 2,
                        sectionId = 2,
                        address = "вул. Волейбольна, 20",
                        workingDays = "Пн-Сб, 8:00-17:00",
                        phoneNumber = "0631122334",
                        price = 450,
                        isSelected = false
                    ),
                    DbSectionDetails(
                        id = 3,
                        sectionId = 3,
                        address = "вул. Тенісна, 8",
                        workingDays = "Пн-Пт, 9:00-19:00",
                        phoneNumber = "0662233445",
                        price = 600,
                        isSelected = false
                    ),
                    DbSectionDetails(
                        id = 4,
                        sectionId = 1,
                        address = "вул. Баскетбольна, 18",
                        workingDays = "Пн-Пт, 9:00-18:00",
                        phoneNumber = "0509876543",
                        price = 420,
                        isSelected = false
                    )
                )

                db.SectionDetailsDao().addSectionDetails(*sectionDetailsList.toTypedArray())
            }

        }
    }
}