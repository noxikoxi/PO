package com.example.po

import org.springframework.context.annotation.Lazy
import org.springframework.stereotype.Service

@Service
@Lazy
class AuthServiceLazy {

    private val users = mapOf(
        "admin" to "admin",
        "user" to "user",
        "quest" to "quest"
    )

    init {
        println("🔵 AuthServiceLazy został zainicjalizowany!")
    }

    fun authenticate(username: String, password: String): Boolean {
        return users[username] == password;
    }
}