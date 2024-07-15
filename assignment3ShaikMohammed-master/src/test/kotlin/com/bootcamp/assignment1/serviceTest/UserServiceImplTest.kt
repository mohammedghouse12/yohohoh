package com.bootcamp.assignment1.serviceTest

import com.bootcamp.assignment1.entity.User
import com.bootcamp.assignment1.exception.ResourceNotFoundException
import com.bootcamp.assignment1.repository.UserRepository
import com.bootcamp.assignment1.service.UserService
import com.bootcamp.assignment1.service.implementations.UserServiceImpl
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import jdk.internal.classfile.impl.verifier.VerifierImpl.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.times
import java.util.concurrent.ConcurrentHashMap
import kotlin.test.assertEquals
import io.mockk.verify

@ExtendWith(MockKExtension::class)
class UserServiceImplTest {

    @MockK
    private lateinit var userRepository: UserRepository

    private lateinit var userService: UserService

    @BeforeEach
    fun setUp(){
        userService = UserServiceImpl(userRepository)
    }

    @Test
    fun testFindAll(){
        val users = ConcurrentHashMap<Int, User>().apply {
            put(1, User(1, "Alice", "USA", "alice@navi.com"))
            put(2, User(2, "Bob", "UK", "bob@navi.com"))
            put(3, User(3, "Charlie", "USA", "charlie@navi.com"))
        }

        every{ userRepository.findAllUsers()} returns users

        val result = userService.findAll()

        assertEquals(result, users.values.toList())
    }

    @Test
    fun testFindUserByIdIfUserExists(){
        val users = ConcurrentHashMap<Int, User>().apply {
            put(1, User(1, "Alice", "USA", "alice@navi.com"))
            put(2, User(2, "Bob", "UK", "bob@navi.com"))
            put(3, User(3, "Charlie", "USA", "charlie@navi.com"))
        }

        every{ userRepository.findAllUsers()} returns users

        val userId = 1

        val result = userService.findUserById(userId)

        assertEquals(result, users.values.toList().get(userId-1))
    }

    @Test
    fun testFindUserByIdIfUserDoesNotExists(){
        val users = ConcurrentHashMap<Int, User>().apply {
            put(1, User(1, "Alice", "USA", "alice@navi.com"))
            put(2, User(2, "Bob", "UK", "bob@navi.com"))
            put(3, User(3, "Charlie", "USA", "charlie@navi.com"))
        }

        every{ userRepository.findAllUsers()} returns users

        val userId = 4

        val exception = assertThrows(ResourceNotFoundException::class.java) {
            val result = userService.findUserById(userId)
        }

        assertEquals("User with id $userId not found", exception.message)
    }

    @Test
    fun testFindUserByEmailIfUserExists(){
        val users = ConcurrentHashMap<Int, User>().apply {
            put(1, User(1, "Alice", "USA", "alice@navi.com"))
            put(2, User(2, "Bob", "UK", "bob@navi.com"))
            put(3, User(3, "Charlie", "USA", "charlie@navi.com"))
        }

        every{ userRepository.findAllUsers()} returns users

        val email = "alice@navi.com"

        val result = userService.findUserByEmail(email)

        assertEquals(result, users.values.toList().get(0))
    }

    @Test
    fun testFindUserByEmailIfUserDoesNotExists(){
        val users = ConcurrentHashMap<Int, User>().apply {
            put(1, User(1, "Alice", "USA", "alice@navi.com"))
            put(2, User(2, "Bob", "UK", "bob@navi.com"))
            put(3, User(3, "Charlie", "USA", "charlie@navi.com"))
        }

        every{ userRepository.findAllUsers()} returns users

        val email = "chandler@navi.com"

        val exception = assertThrows(ResourceNotFoundException::class.java){
            val result = userService.findUserByEmail(email)
        }

        assertEquals(exception.message, ("User with email $email not found"))
    }

    @Test
    fun testFindUsersByNationality(){
        val users = ConcurrentHashMap<Int, User>().apply {
            put(1, User(1, "Alice", "USA", "alice@navi.com"))
            put(2, User(2, "Bob", "UK", "bob@navi.com"))
            put(3, User(3, "Charlie", "USA", "charlie@navi.com"))
        }

        val expected = mutableListOf<User>().apply {
            add(users[1]!!)
            add(users[3]!!)
        }

        every{ userRepository.findAllUsers()} returns users

        val nationality = "USA"
        val result = userService.findUsersByNationality(nationality)

        assertEquals(result, expected)
    }

    @Test
    fun testFailsToFindUsersByNationality(){
        val users = ConcurrentHashMap<Int, User>().apply {
            put(1, User(1, "Alice", "USA", "alice@navi.com"))
            put(2, User(2, "Bob", "UK", "bob@navi.com"))
            put(3, User(3, "Charlie", "USA", "charlie@navi.com"))
        }

        val expected = mutableListOf<User>().apply {
            add(users[1]!!)
            add(users[3]!!)
        }

        every{ userRepository.findAllUsers()} returns users

        val nationality = "Australia"

        val exception = assertThrows(ResourceNotFoundException::class.java){
            val result = userService.findUsersByNationality(nationality)
        }

        assertEquals(exception.message, ("User with Nationality $nationality not found"))
    }

//    @Test
//    fun expectsToDelete(){
//        val users = ConcurrentHashMap<Int, User>().apply {
//            put(1, User(1, "Alice", "USA", "alice@navi.com"))
//        }
//
//        every{ userRepository.findAllUsers()} returns users
//
//        userService.deleteUser(1)
//        verify(exactly = 1) { userRepository.deleteUser(users[0]!!) }
//
//    }
}