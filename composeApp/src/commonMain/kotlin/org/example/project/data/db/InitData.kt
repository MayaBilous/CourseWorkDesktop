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
                    DbLogin(id = 0, userName = "тренер", password = "123", isAdmin = true),
                    DbLogin(id = 1, userName = "учень", password = "321", isAdmin = false),
                )
            }
            if (db.SportSectionDao().getSectionsWithDetails().isEmpty()) {
                val sportSection1 = DbSportSection(id = 0, sectionName = "Футбол", sectionInfo = "Футбол — командний вид спорту, в який грають дві команди по одинадцять гравців зі сферичним м'ячем.")
                val sportSection2 = DbSportSection(id = 1, sectionName = "Баскетбол", sectionInfo = "Баскетбол — спортивна командна гра з м'ячем, який закидають руками в кільце із сіткою, закріпленою на щиті на висоті 3 метри 5 сантиметрів над майданчиком.")
                val sportSection3 = DbSportSection(id = 2, sectionName = "Волейбол", sectionInfo = "Волейбол — спортивна гра з м'ячем, у якій дві команди змагаються на спеціальному майданчику, розділеному сіткою.")
                val sportSection4 = DbSportSection(id = 3, sectionName = "Теніс", sectionInfo = "Теніс — вид спорту, у якому грають двоє гравців один проти одного, або дві команди по два гравці в кожній, команда проти команди, на майданчику — корті, поділеному навпіл поперечною сіткою.")
                val sportSection5 = DbSportSection(id = 4, sectionName = "Плавання", sectionInfo = "Плавання — це вид спорту, де спортсмени змагаються у швидкості подолання дистанцій у воді.")
                val sportSection6 = DbSportSection(id = 5, sectionName = "Бокс", sectionInfo = "Бокс — контактний вид спорту, в якому два учасники змагаються, використовуючи лише кулаки.")
                val sportSection7 = DbSportSection(id = 6, sectionName = "Гімнастика", sectionInfo = "Гімнастика — вид спорту, що включає вправи на гнучкість, силу та координацію рухів.")
                val sportSection8 = DbSportSection(id = 7, sectionName = "Карате", sectionInfo = "Карате — це бойове мистецтво, що походить з Японії, яке включає удари руками і ногами.")

                db.SportSectionDao().addSportSection(
                    sportSection1, sportSection2, sportSection3, sportSection4,
                    sportSection5, sportSection6, sportSection7, sportSection8
                )

                val sectionDetailsList = listOf(
                    DbSectionDetails(id = 0, sectionId = 0, address = "вул. Спортивна, 10", workingDays = "Пн-Пт, 9:00-18:00", phoneNumber = "0501234567", price = 500, isSelected = false),
                    DbSectionDetails(id = 1, sectionId = 0, address = "вул. М'ячова, 22", workingDays = "Пн-Сб, 10:00-19:00", phoneNumber = "0500001111", price = 520, isSelected = false),

                    DbSectionDetails(id = 2, sectionId = 1, address = "вул. Баскетбольна, 15", workingDays = "Пн-Пт, 10:00-20:00", phoneNumber = "0509876543", price = 400, isSelected = false),
                    DbSectionDetails(id = 3, sectionId = 1, address = "вул. Баскетбольна, 18", workingDays = "Пн-Пт, 9:00-18:00", phoneNumber = "0509876543", price = 420, isSelected = false),
                    DbSectionDetails(id = 4, sectionId = 1, address = "вул. Кільцева, 7", workingDays = "Сб-Нд, 11:00-16:00", phoneNumber = "0981112222", price = 390, isSelected = false),

                    DbSectionDetails(id = 5, sectionId = 2, address = "вул. Волейбольна, 20", workingDays = "Пн-Сб, 8:00-17:00", phoneNumber = "0631122334", price = 450, isSelected = false),
                    DbSectionDetails(id = 6, sectionId = 2, address = "вул. Мережна, 14", workingDays = "Пн-Пт, 9:00-19:00", phoneNumber = "0935566778", price = 460, isSelected = false),

                    DbSectionDetails(id = 7, sectionId = 3, address = "вул. Тенісна, 8", workingDays = "Пн-Пт, 9:00-19:00", phoneNumber = "0662233445", price = 600, isSelected = false),
                    DbSectionDetails(id = 8, sectionId = 3, address = "вул. Кортова, 5", workingDays = "Пн-Сб, 10:00-18:00", phoneNumber = "0963344556", price = 590, isSelected = false),

                    DbSectionDetails(id = 9, sectionId = 4, address = "вул. Плавальна, 5", workingDays = "Пн-Нд, 7:00-21:00", phoneNumber = "0673344556", price = 550, isSelected = false),
                    DbSectionDetails(id = 10, sectionId = 4, address = "вул. Басейнна, 2А", workingDays = "Пн-Пт, 8:00-18:00", phoneNumber = "0997766554", price = 570, isSelected = false),

                    DbSectionDetails(id = 11, sectionId = 5, address = "вул. Боксерська, 12", workingDays = "Вт-Сб, 10:00-20:00", phoneNumber = "0685566778", price = 470, isSelected = false),
                    DbSectionDetails(id = 12, sectionId = 5, address = "вул. Ринговa, 9", workingDays = "Пн-Пт, 12:00-21:00", phoneNumber = "0974433221", price = 480, isSelected = false),

                    DbSectionDetails(id = 13, sectionId = 6, address = "вул. Гімнастична, 3", workingDays = "Пн-Пт, 8:00-17:00", phoneNumber = "0956677889", price = 430, isSelected = false),
                    DbSectionDetails(id = 14, sectionId = 6, address = "вул. Акробатична, 7", workingDays = "Вт-Нд, 9:00-19:00", phoneNumber = "0933344556", price = 450, isSelected = false),

                    DbSectionDetails(id = 15, sectionId = 7, address = "вул. Карате, 9", workingDays = "Пн-Сб, 10:00-20:00", phoneNumber = "0991122334", price = 490, isSelected = false),
                    DbSectionDetails(id = 16, sectionId = 7, address = "вул. Самурайська, 11", workingDays = "Пн-Пт, 9:00-18:00", phoneNumber = "0678889990", price = 500, isSelected = false)
                )

                db.SectionDetailsDao().addSectionDetails(*sectionDetailsList.toTypedArray())

            }

        }
    }
}