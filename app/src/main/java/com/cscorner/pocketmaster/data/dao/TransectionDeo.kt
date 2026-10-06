package com.cscorner.pocketmaster.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.cscorner.pocketmaster.data.model.CategoryTotal
import com.cscorner.pocketmaster.data.model.Transection
import com.cscorner.pocketmaster.data.model.TransectionType
import kotlinx.coroutines.flow.Flow

@Dao
interface TransectionDeo {
    @Query("SELECT * FROM transection ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<Transection>>

    @Query("SELECT * FROM transection WHERE type = :type ORDER BY date DESC")
    fun getTransactionsByType(type: TransectionType): Flow<List<Transection>>

    @Query("SELECT CAST(SUM(amount) as DOUBLE) FROM transection WHERE type = :type")
    suspend fun getTotalByType(type: TransectionType): Double?
//
//    @Query("SELECT * FROM transection")
//    suspend fun getAllTransactionsList(): List<Transection>

    @Query("""
    SELECT
        category AS categoryName,
        SUM(amount) AS totalAmount
    FROM transection
    WHERE type = :type
      AND date >= :startDate
    GROUP BY category
    ORDER BY totalAmount DESC
""")
    fun getCategoryTotals(
        type: TransectionType,
        startDate: Long
    ): Flow<List<CategoryTotal>>

    @Insert
    suspend fun insert(transaction: Transection)

    @Delete
    suspend fun delete(transaction: Transection)
}