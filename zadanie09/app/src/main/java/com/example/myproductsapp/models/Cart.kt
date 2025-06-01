package com.example.myproductsapp.models

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class Cart : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var items: RealmList<CartItem> = realmListOf()
}
