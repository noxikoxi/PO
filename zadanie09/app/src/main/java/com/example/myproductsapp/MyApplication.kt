package com.example.myproductsapp

import android.app.Application
import com.example.myproductsapp.models.Cart
import com.example.myproductsapp.models.CartItem
import com.example.myproductsapp.models.Category
import com.example.myproductsapp.models.Product
import com.example.myproductsapp.models.RealmDataInitializer
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyApplication : Application() {

    // Używamy obiektu towarzyszącego (companion object) jako prostego Singletona
    // aby Realm był dostępny globalnie.
    companion object {
        lateinit var realm: Realm
    }

    override fun onCreate() {
        super.onCreate()
        initRealm()

        // Dodaje Dane
        CoroutineScope(Dispatchers.Main).launch { // Użyj Dispatchers.IO dla operacji bazodanowych
            RealmDataInitializer.initialize(realm)
        }
    }

    private fun initRealm() {
        val config = RealmConfiguration.Builder(schema = setOf(
            Product::class,
            Category::class,
            CartItem::class,
            Cart::class
        ))
            .schemaVersion(2)
            .build()
        realm = Realm.open(config)
    }
}