import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.cscorner.pocketmaster.data.database.AppDatabase
import com.cscorner.pocketmaster.data.model.CategoryTotal
import com.cscorner.pocketmaster.data.model.TransectionType
import com.cscorner.pocketmaster.data.repository.FinanceRepository
import com.example.pocketmaster.data.model.DashboardState
import com.example.pocketmaster.data.model.ExpenseCategoryData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FinanceRepository = FinanceRepository(
        AppDatabase.getDatabase(application).transectionDeo(),
        AppDatabase.getDatabase(application).categoryDao()
    )

    val dashboardState: StateFlow<DashboardState> = combine(
        flow {
            emit(repository.getTotalByType(TransectionType.INCOME))
        },
        flow {
            emit(repository.getTotalByType(TransectionType.EXPENSE))
        },
        repository.getCategoryTotals(TransectionType.EXPENSE)
    ) { income: Double, expense: Double, categoryTotals: List<CategoryTotal> ->

        DashboardState(
            totalIncome = income,
            totalExpense = expense,
            balance = income - expense,
            expenseCategories = categoryTotals.map { categoryTotal ->

                ExpenseCategoryData(
                    category = categoryTotal.categoryName,
                    amount = categoryTotal.totalAmount,
                    percentage = TODO()
                )
            }
        )

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = DashboardState()
    )
}