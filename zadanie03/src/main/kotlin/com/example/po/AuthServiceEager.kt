package com.example.po

import org.springframework.stereotype.Service

@Service
class AuthServiceEager {
    private val users = mapOf(
        "admin" to "admin",
        "user" to "user",
        "quest" to "quest"
    )

    init {
        println("🔵 AuthServiceEager został zainicjalizowany!")
    }

    fun authenticate(username: String, password: String): Boolean {
        return users[username] == password
    }
}