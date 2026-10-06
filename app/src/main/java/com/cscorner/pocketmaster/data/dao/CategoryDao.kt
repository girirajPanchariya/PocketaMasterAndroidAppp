package com.example.pocketmaster.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.cscorner.pocketmaster.data.model.Category
import com.cscorner.pocketmaster.data.model.CategoryTotal
import com.cscorner.pocketmaster.data.model.TransectionType

import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories")
    fun getAllCategories(): Flow<List<Category>>

    @Query("SELECT * FROM categories WHERE type = :type")
    fun getCategoriesByType(type: TransectionType): Flow<List<Category>>

    @Query("""
        SELECT
            c.name AS categoryName,
            COALESCE(SUM(t.amount), 0.0) AS totalAmount
        FROM categories c
        LEFT JOIN transection t
            ON c.name = t.category
            AND t.type = :type
        WHERE c.type = :type
          AND (t.date >= :startDate OR t.date IS NULL)
        GROUP BY c.name
        ORDER BY totalAmount DESC
    """)
    fun getCategoryTotals(
        type: TransectionType,
        startDate: Long
    ): Flow<List<CategoryTotal>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: Category)
}