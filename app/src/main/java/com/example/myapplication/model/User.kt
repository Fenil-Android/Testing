package com.example.myapplication.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// Domain Model
data class User(
    val id: Int,
    val name: String,
    val email: String,
    val companyName: String
)

// Network DTO
data class UserDto(
    val id: Int,
    val name: String,
    val email: String,
    val company: CompanyDto
)

data class CompanyDto(val name: String)

// Room Entity
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val email: String,
    val companyName: String
)

// Mapping extensions
fun UserDto.toEntity() = UserEntity(id, name, email, company.name)
fun UserEntity.toDomain() = User(id, name, email, companyName)

