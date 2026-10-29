package com.example.myapplication.repository

import com.example.myapplication.api.UserApi
import com.example.myapplication.api.UserDao
import com.example.myapplication.model.User
import com.example.myapplication.model.toDomain
import com.example.myapplication.model.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepository(
    private val api: UserApi,
    private val dao: UserDao
) {
    fun getUsers(query: String): Flow<List<User>> =
        dao.getUsers(query).map { entities -> entities.map { it.toDomain() } }

    suspend fun refreshUsers() {
        val remoteUsers = api.getUsers()
        dao.saveUsers(remoteUsers.map { it.toEntity() })
    }

    suspend fun getUserById(id: Int): User? =
        dao.getUserById(id)?.toDomain()
}