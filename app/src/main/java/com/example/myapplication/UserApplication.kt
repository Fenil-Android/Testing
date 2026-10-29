package com.example.myapplication

import android.app.Application
import androidx.room.Room
import com.example.myapplication.api.AppDatabase
import com.example.myapplication.api.UserApi
import com.example.myapplication.repository.UserRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.getValue

class UserApplication : Application() {

    // Manual Dependency Injection
    private val database by lazy {
        Room.databaseBuilder(this, AppDatabase::class.java, "user_db").build()
    }

    private val api by lazy {
        Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserApi::class.java)
    }

    val repository by lazy {
        UserRepository(api, database.userDao())
    }
}
