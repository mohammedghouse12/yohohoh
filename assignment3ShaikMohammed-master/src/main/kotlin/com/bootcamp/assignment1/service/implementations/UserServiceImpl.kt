package com.bootcamp.assignment1.service.implementations

import com.bootcamp.assignment1.entity.User
import com.bootcamp.assignment1.exception.ResourceNotFoundException
import com.bootcamp.assignment1.repository.UserRepository
import com.bootcamp.assignment1.service.UserService
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(val userRepository: UserRepository) : UserService {

    override fun findAll(): List<User> {
        val usersById = userRepository.findAllUsers()
        return usersById.values.toList()
    }

    override fun addUser(user: User) : User{
        return userRepository.addUser(user)
    }

    override fun findUserById(id: Int): User {
        val usersById  = userRepository.findAllUsers()
        return usersById[id] ?: throw ResourceNotFoundException("User with id $id not found")
    }

    override fun findUserByEmail(email : String): User {
        val users = userRepository.findAllUsers().values.toList()
        for(user in users)
            if(user.email==email) return user
        throw ResourceNotFoundException("User with email $email not found")
    }

    override fun findUsersByNationality(nationality: String): List<User> {
        val usersById = userRepository.findAllUsers()
        val allUsers =  usersById.values.toList()

        val result = mutableListOf<User>()

        for (user in allUsers)
            if (user.nationality == nationality) result.add(user)

        return result
    }

    override fun deleteUser(id: Int) : Unit{
        userRepository.deleteUser(findUserById(id))
    }

    override fun updateUser(user: User) : User{
        val existingUser = findUserById(user.id) ?: throw ResourceNotFoundException("User with id ${user.id} not found")

        if(existingUser.email!=user.email && userRepository.isEmailPresent(user.email)){
            throw IllegalArgumentException("Email ${user.email} already exists")
        }

        existingUser.apply {
            name = user.name ?: name
            nationality = user.nationality ?: nationality
        }

        return userRepository.updateUser(user, existingUser)
    }
}
