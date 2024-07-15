package com.bootcamp.assignment1.repository

import com.bootcamp.assignment1.entity.User
import java.util.concurrent.ConcurrentHashMap


interface UserRepository {
    fun findAllUsers(): ConcurrentHashMap<Int, User>
    fun addUser(user: User) : User
    fun deleteUser(user: User): Unit
    fun updateUser(user : User, existingUser: User): User
    fun isEmailPresent(email : String) : Boolean
}