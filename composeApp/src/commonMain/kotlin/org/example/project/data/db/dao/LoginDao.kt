package org.example.project.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import org.example.project.data.db.model.DbLogin

@Dao
interface LoginDao {

    @Query("SELECT * FROM dblogin")
    suspend fun getAll(): List<DbLogin>

    @Insert
    suspend fun insert(vararg dbLogin: DbLogin)
}
