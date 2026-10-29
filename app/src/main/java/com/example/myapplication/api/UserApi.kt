package com.example.myapplication.api

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Upsert
import com.example.myapplication.model.UserDto
import com.example.myapplication.model.UserEntity
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET

interface UserApi {
    @GET("users")
    suspend fun getUsers(): List<UserDto>
}

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE name LIKE '%' || :query || '%'")
    fun getUsers(query: String): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: Int): UserEntity?

    @Upsert
    suspend fun saveUsers(users: List<UserEntity>)
}

@Database(entities = [UserEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}