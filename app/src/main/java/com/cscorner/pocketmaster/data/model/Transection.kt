package com.cscorner.pocketmaster.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transection")
data class Transection(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val amount: Double,
    val description: String,
    val category: String,
    val type: TransectionType,
    val date: Long = System.currentTimeMillis()
)
