package com.cscorner.pocketmaster.data.database

import android.content.Context
import android.graphics.Color
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.cscorner.pocketmaster.data.dao.TransectionDeo
import com.cscorner.pocketmaster.data.model.Category
import com.cscorner.pocketmaster.data.model.Transection
import com.cscorner.pocketmaster.data.model.TransectionType
import com.example.pocketmaster.data.dao.CategoryDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        Transection::class,
        Category::class
    ],
    version = 1,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transectionDeo(): TransectionDeo
    abstract fun categoryDao(): CategoryDao


    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "finance_database"
                ).apply {
                    addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Add default categories
                            INSTANCE?.let { database ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    val categoryDao = database.categoryDao()
                                    // Default expense categories
                                    categoryDao.insert(Category("Food & Dining", TransectionType.EXPENSE,
                                        Color.parseColor("#FF5252")))
                                    categoryDao.insert(Category("Transportation", TransectionType.EXPENSE, Color.parseColor("#448AFF")))
                                    categoryDao.insert(Category("Shopping", TransectionType.EXPENSE, Color.parseColor("#4CAF50")))
                                    categoryDao.insert(Category("Bills & Utilities", TransectionType.EXPENSE, Color.parseColor("#FFC107")))
                                    categoryDao.insert(Category("Healthcare", TransectionType.EXPENSE, Color.parseColor("#E040FB")))

                                    // Default income categories
                                    categoryDao.insert(Category("Salary", TransectionType.INCOME, Color.parseColor("#00BCD4")))
                                    categoryDao.insert(Category("Freelance", TransectionType.INCOME, Color.parseColor("#9C27B0")))
                                    categoryDao.insert(Category("Investments", TransectionType.INCOME, Color.parseColor("#4CAF50")))
                                }
                            }
                        }
                    })
                }.build()
                INSTANCE = instance
                instance
            }
        }

    }


}