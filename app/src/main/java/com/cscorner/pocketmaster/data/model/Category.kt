package com.cscorner.pocketmaster.data.model


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey
    val name: String,
    val type: TransectionType,
    val color: Int
)
