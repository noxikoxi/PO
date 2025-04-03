package com.example.po

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Lazy
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class AuthController @Autowired  constructor(
    private val authServiceEager: AuthServiceEager,
    @Lazy private val authServiceLazy: AuthServiceLazy
) {
    private val sampleData = listOf(
        Product(1, "Telewizor", 12500.0),
        Product(2, "Laptop", 3000.0),
        Product(3, "Telefon", 2500.0),
        Product(4, "Słuchawki", 500.0),
        Product(5, "Wanna", 1250.50)
    )

    @GetMapping("/data")
    fun getData(): ResponseEntity<List<Product>> {
        return ResponseEntity.ok(sampleData)
    }

    @PostMapping("/eager/login")
    fun login(@RequestBody credentials: UserCredentials): ResponseEntity<Map<String, Any>> {
        val isAuthenticated = authServiceEager.authenticate(credentials.username, credentials.password)
        val response = mapOf(
            "success" to isAuthenticated,
            "message" to if (isAuthenticated) "Login successful" else "Invalid credentials"
        )
        return ResponseEntity.ok(response)
    }

    @PostMapping("/lazy/login")
    fun loginLazy(@RequestBody credentials: UserCredentials): ResponseEntity<Map<String, Any>> {
        val isAuthenticated = authServiceLazy.authenticate(credentials.username, credentials.password)
        val response = mapOf(
            "success" to isAuthenticated,
            "message" to if (isAuthenticated) "Login successful" else "Invalid credentials"
        )
        return ResponseEntity.ok(response)
    }
}