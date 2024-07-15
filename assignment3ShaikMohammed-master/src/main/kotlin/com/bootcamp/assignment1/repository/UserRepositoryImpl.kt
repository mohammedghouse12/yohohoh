package com.bootcamp.assignment1.repository

import com.bootcamp.assignment1.entity.User
import com.bootcamp.assignment1.entity.UserDetails
import com.bootcamp.assignment1.exception.ResourceNotFoundException
import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap
import java.util.*

@Repository
class UserRepositoryImpl : UserRepository {

    private val usersById = ConcurrentHashMap<Int, User>()
    private val usersByEmail = ConcurrentHashMap<String, User>()
    private val userDetailsByUserId = ConcurrentHashMap<Int, UserDetails>()

    private var uniqueId = 1

    override fun findAllUsers(): ConcurrentHashMap<Int, User>{
        return usersById
    }

    override fun addUser(user: User): User {
        if (usersByEmail.containsKey(user.email)) {
            throw IllegalArgumentException("Email ${user.email} already exists")
        }

        user.id = uniqueId++
        usersById[user.id] = user
        usersByEmail[user.email] = user

        return user
    }

    override fun deleteUser(user: User): Unit {
        usersByEmail.remove(user.email)
    }

    override fun updateUser(user : User, existingUser: User): User {
        if(user.email !=existingUser.email){
            usersByEmail.remove(existingUser.email)
            existingUser.email = user.email ?: existingUser.email
            usersByEmail[user.email] = existingUser
        }

        usersById[user.id] = existingUser

        return existingUser
    }

    override fun isEmailPresent(email : String) : Boolean{
        return usersByEmail.containsKey(email)
    }
}