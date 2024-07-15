package com.bootcamp.assignment1.entity

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class User(
    var id: Int,

    @field:NotBlank(message = "Name must not be blank")
    var name: String,

    @field:NotBlank(message = "Nationality must not be blank")
    var nationality: String,

    @field:NotBlank(message = "Email must not be blank")
    @field:Email(message = "Email should be valid")
    var email: String
)
