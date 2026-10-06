package com.cscorner.pocketmaster.ui.dashboard

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.cscorner.pocketmaster.databinding.FragmentDashboardBinding
import com.cscorner.pocketmaster.ui.viewmodel.FinanceViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    // Shared FinanceViewModel
    private val financeViewModel: FinanceViewModel by viewModels(
        ownerProducer = { requireActivity() })

    private val currencyFormatter = NumberFormat.getCurrencyInstance(Locale.getDefault())


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDashboardBinding.inflate(
            inflater, container, false
        )

        return binding.root
    }


    override fun onViewCreated(
        view: View, savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        setupPieChart()

        observeTransactions()
    }


    // ============================================================
    // PIE CHART SETUP
    // ============================================================

    private fun setupPieChart() {

        binding.pieChart.apply {

            description.isEnabled = false

            isDrawHoleEnabled = true

            setHoleColor(Color.TRANSPARENT)

            setExtraOffsets(
                24f, 24f, 24f, 24f
            )

            holeRadius = 56f

            setUsePercentValues(true)

            setEntryLabelColor(Color.WHITE)

            setEntryLabelTextSize(12f)

            // Hide text directly on slices
            setDrawEntryLabels(false)


            // ====================================================
            // LEGEND
            // ====================================================

            legend.apply {

                isEnabled = true

                orientation = Legend.LegendOrientation.VERTICAL

                horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT

                verticalAlignment = Legend.LegendVerticalAlignment.TOP

                setDrawInside(false)

                xEntrySpace = 10f

                yEntrySpace = 0f

                yOffset = -50f

                xOffset = 10f

                textSize = 12f

                formSize = 12f

                textColor = Color.WHITE
            }


            animateY(
                1400, Easing.EaseInOutCirc
            )
        }
    }


    // ============================================================
    // OBSERVE TRANSACTIONS
    // ============================================================

    private fun observeTransactions() {

        viewLifecycleOwner.lifecycleScope.launch {

            financeViewModel.allTransactions.collect { transactions ->

                if (transactions.isEmpty()) {

                    binding.tvBalance.text = currencyFormatter.format(0.0)

                    binding.tvTotalIncome.text = currencyFormatter.format(0.0)

                    binding.tvTotalExpenses.text = currencyFormatter.format(0.0)

                    return@collect
                }


                // -----------------------------------------------
                // Calculate Income
                // -----------------------------------------------

                val totalIncome = transactions.filter {
                    it.type.name == "INCOME"
                }.sumOf {
                    it.amount
                }


                // -----------------------------------------------
                // Calculate Expense
                // -----------------------------------------------

                val totalExpense = transactions.filter {
                    it.type.name == "EXPENSE"
                }.sumOf {
                    it.amount
                }


                // -----------------------------------------------
                // Calculate Balance
                // -----------------------------------------------

                val balance = totalIncome - totalExpense


                // -----------------------------------------------
                // Update UI
                // -----------------------------------------------

                binding.tvBalance.text = currencyFormatter.format(balance)

                binding.tvTotalIncome.text = currencyFormatter.format(totalIncome)

                binding.tvTotalExpenses.text = currencyFormatter.format(totalExpense)


                // -----------------------------------------------
                // Expense categories
                // -----------------------------------------------

                val expenseCategories = transactions.filter {
                    it.type.name == "EXPENSE"
                }.groupBy {
                    it.category
                }.map { (category, transactionList) ->

                    PieEntry(
                        transactionList.sumOf { it.amount }.toFloat(),

                        category
                    )
                }


                updatePieChart(
                    expenseCategories
                )
            }
        }
    }


    // ============================================================
    // UPDATE PIE CHART
    // ============================================================

    private fun updatePieChart(
        entries: List<PieEntry>
    ) {

        if (entries.isEmpty()) {

            binding.pieChart.clear()

            return
        }


        val colors = listOf(

            Color.rgb(
                64, 89, 128
            ),

            Color.rgb(
                149, 165, 124
            ),

            Color.rgb(
                217, 184, 162
            ),

            Color.rgb(
                191, 134, 134
            ),

            Color.rgb(
                179, 48, 80
            ),

            Color.rgb(
                193, 37, 82
            ),

            Color.rgb(
                255, 102, 0
            )
        )


        val dataSet = PieDataSet(entries, "" )


        dataSet.apply {

            this.colors = colors

            sliceSpace = 3f

            selectionShift = 5f

            valueTextSize = 0f

            valueTextColor = Color.WHITE
        }


        val pieData = PieData(dataSet
        )


        pieData.setDrawValues(false)


        binding.pieChart.apply {

            data = pieData

            invalidate()

            animateY(
                1000, Easing.EaseInOutCirc
            )
        }
    }


    // ============================================================
    // DESTROY
    // ============================================================

    override fun onDestroyView() {

        super.onDestroyView()

        _binding = null
    }
}