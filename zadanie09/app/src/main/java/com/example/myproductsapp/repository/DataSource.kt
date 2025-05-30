package com.example.myproductsapp.repository

import com.example.myproductsapp.models.Category
import com.example.myproductsapp.models.Product

object DataSource {
    private val allProducts = listOf(
        Product(id = "p1", name = "Smartfon XYZ", description = "Najnowszy smartfon z potężnym procesorem.", price = 1299.99, categoryId = "1"),
        Product(id = "p2", name = "Laptop Gamingowy", description = "Laptop dla graczy z kartą RTX.", price = 4500.00, categoryId = "1"),
        Product(id = "p3", name = "Słuchawki Bluetooth", description = "Bezprzewodowe słuchawki z redukcją szumów.", price = 349.50, categoryId = "1"),

        Product(id = "p4", name = "T-shirt bawełniany", description = "Wygodny T-shirt 100% bawełna.", price = 49.99, categoryId = "2"),
        Product(id = "p5", name = "Jeansy Slim Fit", description = "Modne jeansy męskie.", price = 199.00, categoryId = "2"),
        Product(id = "p6", name = "Kurtka Zimowa", description = "Ciepła kurtka na mroźne dni.", price = 399.00, categoryId = "2"),

        Product(id = "p7", name = "Wiedźmin Ostatnie Życzenie", description = "Zbiór opowiadań Andrzeja Sapkowskiego.", price = 35.00, categoryId = "3"),
        Product(id = "p8", name = "Książka kucharska", description = "Przepisy na szybkie i zdrowe posiłki.", price = 60.00, categoryId = "3"),

        Product(id = "p9", name = "Kosiarka elektryczna", description = "Cicha i wydajna kosiarka do małego ogrodu.", price = 750.00, categoryId = "4"),
        Product(id = "p10", name = "Zestaw narzędzi", description = "Kompletny zestaw dla majsterkowicza.", price = 150.00, categoryId = "4"),

        Product(id = "p11", name = "Rower Górski", description = "Solidny rower na górskie szlaki.", price = 1500.00, categoryId = "5"),
        Product(id = "p12", name = "Hantelki 5kg", description = "Idealne do ćwiczeń w domu.", price = 99.00, categoryId = "5"),

        Product(id = "p13", name = "Krem Nawilżający", description = "Do skóry suchej i wrażliwej.", price = 89.00, categoryId = "6"),
        Product(id = "p14", name = "Szampon Wzmacniający", description = "Dla zdrowych i lśniących włosów.", price = 25.00, categoryId = "6")
    )

    fun getCategories(): List<Category> {
        return listOf(
            Category(id = "1", name = "Elektronika"),
            Category(id = "2", name = "Ubrania"),
            Category(id = "3", name = "Książki"),
            Category(id = "4", name = "Dom i Ogród"),
            Category(id = "5", name = "Sport i Rekreacja"),
            Category(id = "6", name = "Zdrowie i Uroda")
        )
    }

    fun getAllProducts(categoryId: String?): List<Product> {
        if(categoryId == null) return allProducts
        return allProducts.filter { it.categoryId == categoryId }
    }
}