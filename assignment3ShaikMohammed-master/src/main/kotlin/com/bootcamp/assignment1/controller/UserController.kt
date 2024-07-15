package com.bootcamp.assignment1.controller

import com.bootcamp.assignment1.entity.User
import com.bootcamp.assignment1.exception.ResourceNotFoundException
import com.bootcamp.assignment1.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/users")
class UserController (val userService: UserService){

//    @GetMapping
//    fun findAllUsers() : ResponseEntity<Any>{
//        val result : List<User> = userService.findAll()
//        return ResponseEntity.status(HttpStatus.OK).body(result)
//    }

    @PostMapping
    fun addUser(@RequestBody @Valid user : User) : ResponseEntity<Any>{
        try{
            val result : User = userService.addUser(user)
            return ResponseEntity.status(HttpStatus.CREATED).body(result)
        }catch (exec : IllegalArgumentException){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(exec.message)
        }
    }

    @GetMapping("/{id}")
    fun findUserById(@PathVariable id : Int) : ResponseEntity<Any>{
        try{
            val result : User = userService.findUserById(id)
            return ResponseEntity.status(HttpStatus.OK).body(result)
        }catch (exec : ResourceNotFoundException){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exec.message)
        }
    }

    @GetMapping("/email/{email}")
    fun findUserById(@PathVariable email : String) : ResponseEntity<Any> {
        try {
            val result: User = userService.findUserByEmail(email)
            return ResponseEntity.status(HttpStatus.OK).body(result)
        } catch (exec: ResourceNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exec.message)
        }
    }

    @GetMapping
    fun getUsers(@RequestParam(required = false) nationality: String?): ResponseEntity<Any> {
        if (nationality.isNullOrBlank()) {
            try{
                val result = userService.findAll()
                return ResponseEntity.status(HttpStatus.OK).body(result)
            }catch (exec : ResourceNotFoundException){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exec.message)
            }
        }
        else {
            val result = userService.findUsersByNationality(nationality)
            if (result.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Users with nationality '$nationality' not found.")
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(result)
            }
        }
    }

    @DeleteMapping("/{id}")
    fun deleteUserById(@PathVariable id : Int) : ResponseEntity<Any>{
        try{
            userService.deleteUser(id)
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Successfully deleted user with Id $id")
        }catch(exec : ResourceNotFoundException){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exec.message)
        }
    }

    @PutMapping("/{id}")
    fun updateUserById(@PathVariable id : Int, @RequestBody user: User) : ResponseEntity<Any>{
        user.id = id
        try{
            val result : User = userService.updateUser(user)
            return ResponseEntity.status(HttpStatus.OK).body(result)
        }catch (exec : ResourceNotFoundException){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exec.message)
        }catch(exec : IllegalArgumentException){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(exec.message)
        }

    }
}