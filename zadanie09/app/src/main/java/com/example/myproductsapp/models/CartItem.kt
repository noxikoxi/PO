package com.example.myproductsapp.models

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class CartItem : RealmObject{
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var product: Product? = null
    var quantity: Int = 0
}

data class CartItemUI(
    val id: ObjectId,
    val productName: String,
    val productPrice: Double,
    val quantity: Int
)