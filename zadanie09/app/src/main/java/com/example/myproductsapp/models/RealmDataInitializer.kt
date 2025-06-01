package com.example.myproductsapp.models

import android.util.Log
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object RealmDataInitializer {

    suspend fun initialize(realm: Realm) {
        withContext(Dispatchers.IO) { // Przenosimy operacje bazodanowe do wątku IO
            val existingCategoriesCount = realm.query<Category>().count().find()

            if (existingCategoriesCount == 0L) { // Sprawdzamy, czy baza jest pusta
                realm.write {
                    Log.d("RealmInitializer", "Rozpoczynam inicjalizację danych Realm...")

                    // Kategorie
                    val categoriesData = listOf(
                        Category().apply { name = "Elektronika" },
                        Category().apply { name = "Ubrania" },
                        Category().apply { name = "Książki" },
                        Category().apply { name = "Dom i Ogród" },
                        Category().apply { name = "Sport i Rekreacja" },
                        Category().apply { name = "Zdrowie i Uroda" }
                    )

                    // Mapa do szybkiego wyszukiwania obiektów Category po nazwie
                    val categoryMap = mutableMapOf<String, Category>()
                    categoriesData.forEach { cat ->
                        val managedCategory = copyToRealm(cat)
                        categoryMap[cat.name] = managedCategory
                        Log.d("RealmInitializer", "Dodano kategorię: ${cat.name}")
                    }

                    val productsData = listOf(
                        Product().apply { name = "Smartfon XYZ"; description = "Najnowszy smartfon z potężnym procesorem."; price = 1299.99; category = findLatest(categoryMap["Elektronika"]!!) },
                        Product().apply { name = "Laptop Gamingowy"; description = "Laptop dla graczy z kartą RTX."; price = 4500.00; category = findLatest(categoryMap["Elektronika"]!!) },
                        Product().apply { name = "Słuchawki Bluetooth"; description = "Bezprzewodowe słuchawki z redukcją szumów."; price = 349.50; category = findLatest(categoryMap["Elektronika"]!!) },

                        Product().apply { name = "T-shirt bawełniany"; description = "Wygodny T-shirt 100% bawełna."; price = 49.99; category = findLatest(categoryMap["Ubrania"]!!) },
                        Product().apply { name = "Jeansy Slim Fit"; description = "Modne jeansy męskie."; price = 199.00; category = findLatest(categoryMap["Ubrania"]!!) },
                        Product().apply { name = "Kurtka Zimowa"; description = "Ciepła kurtka na mroźne dni."; price = 399.00; category = findLatest(categoryMap["Ubrania"]!!) },

                        Product().apply { name = "Wiedźmin Ostatnie Życzenie"; description = "Zbiór opowiadań Andrzeja Sapkowskiego."; price = 35.00; category = findLatest(categoryMap["Książki"]!!) },
                        Product().apply { name = "Książka kucharska"; description = "Przepisy na szybkie i zdrowe posiłki."; price = 60.00; category = findLatest(categoryMap["Książki"]!!) },

                        Product().apply { name = "Kosiarka elektryczna"; description = "Cicha i wydajna kosiarka do małego ogrodu."; price = 750.00; category = findLatest(categoryMap["Dom i Ogród"]!!) },
                        Product().apply { name = "Zestaw narzędzi"; description = "Kompletny zestaw dla majsterkowicza."; price = 150.00; category = findLatest(categoryMap["Dom i Ogród"]!!) },

                        Product().apply { name = "Rower Górski"; description = "Solidny rower na górskie szlaki."; price = 1500.00; category = findLatest(categoryMap["Sport i Rekreacja"]!!) },
                        Product().apply { name = "Hantelki 5kg"; description = "Idealne do ćwiczeń w domu."; price = 99.00; category = findLatest(categoryMap["Sport i Rekreacja"]!!) },

                        Product().apply { name = "Krem Nawilżający"; description = "Do skóry suchej i wrażliwej."; price = 89.00; category = findLatest(categoryMap["Zdrowie i Uroda"]!!) },
                        Product().apply { name = "Szampon Wzmacniający"; description = "Dla zdrowych i lśniących włosów."; price = 25.00; category = findLatest(categoryMap["Zdrowie i Uroda"]!!) }
                    )

                    productsData.forEach { product ->
                        copyToRealm(product)
                        Log.d("RealmInitializer", "Dodano produkt: ${product.name}")
                    }
                    Log.d("RealmInitializer", "Inicjalizacja danych Realm zakończona pomyślnie.")
                }
            } else {
                Log.d("RealmInitializer", "Dane już istnieją w Realm, pomijam inicjalizację.")
            }
        }
    }
}