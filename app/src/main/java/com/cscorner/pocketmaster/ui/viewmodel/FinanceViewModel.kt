package com.cscorner.pocketmaster.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cscorner.pocketmaster.data.database.AppDatabase
import com.cscorner.pocketmaster.data.model.Category
import com.cscorner.pocketmaster.data.model.Transection
import com.cscorner.pocketmaster.data.model.TransectionType
import com.cscorner.pocketmaster.data.repository.FinanceRepository
import com.example.pocketmaster.data.model.TransactionFilter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class FinanceViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FinanceRepository

    val allTransactions: Flow<List<Transection>>
    val allCategories: Flow<List<Category>>

    init {
        val database: AppDatabase = AppDatabase.getDatabase(application)

        repository = FinanceRepository(
            database.transectionDeo(),
            database.categoryDao()
        )

        allTransactions = repository.allTransetion
        allCategories = repository.Allcategory
    }

    fun getTransactionsByType(type: TransectionType) =
        repository.getTransactionsByType(type)

    fun getCategoriesByType(type: TransectionType) =
        repository.getCategoriesByType(type)

    fun getCategoryTotals(
        type: TransectionType,
        startDate: Long = 0
    ) = repository.getCategoryTotals(type, startDate)

    suspend fun getTotalByType(type: TransectionType) =
        repository.getTotalByType(type)

    fun addTransaction(transaction: Transection) = viewModelScope.launch {
        repository.addTransaction(transaction)
    }

    fun deleteTransaction(transaction: Transection) = viewModelScope.launch {
        repository.deleteTransaction(transaction)
    }

    fun addCategory(category: Category) = viewModelScope.launch {
        repository.addCategory(category)
    }

    // ALL / INCOME / EXPENSE filter
    fun getTransactions(
        filter: TransactionFilter
    ): Flow<List<Transection>> {

        return when (filter) {

            TransactionFilter.ALL -> {
                allTransactions
            }

            TransactionFilter.INCOME -> {
                getTransactionsByType(TransectionType.INCOME)
            }

            TransactionFilter.EXPENSE -> {
                getTransactionsByType(TransectionType.EXPENSE)
            }
        }
    }
}