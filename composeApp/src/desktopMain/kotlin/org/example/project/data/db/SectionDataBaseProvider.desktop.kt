package org.example.project.data.db

actual object SectionDataBaseProvider {

    actual val db: SectionDataBase
        get() = _db

    private lateinit var _db: SectionDataBase

    fun init() {
        _db = getDatabaseBuilder().build()
    }
}