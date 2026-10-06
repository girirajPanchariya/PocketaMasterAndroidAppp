package com.cscorner.pocketmaster.data.repository

import com.cscorner.pocketmaster.data.dao.TransectionDeo
import com.cscorner.pocketmaster.data.model.Category
import com.cscorner.pocketmaster.data.model.Transection
import com.cscorner.pocketmaster.data.model.TransectionType
import com.example.pocketmaster.data.dao.CategoryDao
import kotlinx.coroutines.flow.Flow

class FinanceRepository(
    private val transactionDao: TransectionDeo,
    private val categoryDao: CategoryDao
) {

        val allTransetion = transactionDao.getAllTransactions()
        val Allcategory = categoryDao.getAllCategories()

         fun getTransactionsByType(type: TransectionType) =
        transactionDao.getTransactionsByType(type)

        fun getCategoriesByType(type: TransectionType) =
        categoryDao.getCategoriesByType(type)
    fun getCategoryTotals(type: TransectionType, startDate: Long = 0)=categoryDao.getCategoryTotals(type,startDate)
    suspend fun getTotalByType(type: TransectionType) =
        transactionDao.getTotalByType(type) ?: 0.0
    suspend fun addTransaction(transaction: Transection) {
        transactionDao.insert(transaction)
    }
    suspend fun deleteTransaction(transaction: Transection) {
        transactionDao.delete(transaction)
    }
    suspend fun addCategory(category: Category) {
        categoryDao.insert(category)
    }





}