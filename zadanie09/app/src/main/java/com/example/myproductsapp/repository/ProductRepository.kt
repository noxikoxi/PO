package com.example.myproductsapp.repository

import android.util.Log
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.example.myproductsapp.models.Category
import com.example.myproductsapp.models.Product
import org.mongodb.kbson.ObjectId

class ProductRepository(private val realm: Realm) {

    // Pobieranie wszystkich produktów (jako Flow, aby reagować na zmiany w czasie rzeczywistym)
    fun getAllProducts(): Flow<List<Product>> {
        return realm.query<Product>()
            .asFlow() // Zwraca Flow zmian w wynikach zapytania
            .map { resultsChange: ResultsChange<Product> ->
                resultsChange.list.toList()
            }
    }

    fun getAllProductsByCategory(categoryID: String?) : Flow<List<Product>> {
        val objectId: ObjectId? = categoryID?.let {
            try {
                ObjectId(it) // Konwertujemy String na ObjectId
            } catch (e: IllegalArgumentException) {
                Log.e("ProductRepository", "Błąd konwersji categoryID '$it' na ObjectId: ${e.message}")
                null
            }
        }

        return if (objectId != null) {
            realm.query<Product>("category._id == $0", objectId)
                .asFlow()
                .map { resultsChange: ResultsChange<Product> ->
                    resultsChange.list.toList()
                }
        } else {
            kotlinx.coroutines.flow.flowOf(emptyList())
        }
    }

    // Pobieranie produktu po ID
    fun getProductById(id: ObjectId): Product? {
        return realm.query<Product>("_id == $0", id).first().find()
    }

    // Pobieranie wszystkich kategorii (jako Flow)
    fun getAllCategories(): Flow<List<Category>> {
        return realm.query<Category>()
            .asFlow()
            .map { resultsChange: ResultsChange<Category> ->
                resultsChange.list.toList()
            }
    }

    // Pobieranie kategorii po ID
    fun getCategoryById(id: ObjectId): Category? {
        return realm.query<Category>("_id == $0", id).first().find()
    }

    // Pobieranie kategorii po nazwie (przydatne do dodawania produktów)
    fun getCategoryByName(name: String): Category? {
        return realm.query<Category>("name == $0", name).first().find()
    }


    // Dodawanie produktu
    suspend fun addProduct(product: Product) {
        realm.write {
            copyToRealm(product)
        }
    }

    // Dodawanie kategorii
    suspend fun addCategory(category: Category) {
        realm.write {
            copyToRealm(category)
        }
    }

    suspend fun addProductWithCategory(name: String, description: String, price: Double, categoryName: String) {
        realm.write {
            // Sprawdź, czy kategoria istnieje, jeśli nie, stwórz ją
            var category = query<Category>("name == $0", categoryName).first().find()
            if (category == null) {
                category = copyToRealm(Category().apply { this.name = categoryName })
            }

            // Stwórz i dodaj produkt
            val product = Product().apply {
                this.name = name
                this.description = description
                this.price = price
                this.category = findLatest(category)
            }
            copyToRealm(product)
        }
    }
}