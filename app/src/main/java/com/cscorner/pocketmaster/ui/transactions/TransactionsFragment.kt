package com.cscorner.pocketmaster.ui.transactions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.cscorner.pocketmaster.data.model.Transection
import com.cscorner.pocketmaster.databinding.FragmentTransactionsBinding
import com.cscorner.pocketmaster.ui.viewmodel.FinanceViewModel
import com.example.pocketmaster.data.model.TransactionFilter
import com.example.pocketmaster.ui.transactions.TransactionAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TransactionsFragment : Fragment() {

    private var _binding: FragmentTransactionsBinding? = null
    private val binding get() = _binding!!

    // Use FinanceViewModel
    private val viewModel: FinanceViewModel by viewModels(
        ownerProducer = { requireActivity() }
    )

    private lateinit var transactionAdapter: TransactionAdapter

    // Default filter
    private var currentFilter = TransactionFilter.ALL

    // Used to cancel old Flow collector
    private var transactionsJob: Job? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTransactionsBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }


    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupTabLayout()
        observeTransactions()
    }


    // ============================================================
    // RecyclerView
    // ============================================================

    private fun setupRecyclerView() {

        transactionAdapter = TransactionAdapter(
            onItemClick = { transaction ->

                // Transaction details
                // Add code here later if required

            },

            onDeleteClick = { transaction ->

                viewModel.deleteTransaction(transaction)

            },

            onUndoDelete = { transaction ->

                viewModel.addTransaction(transaction)

            }
        )


        binding.recyclerView.apply {

            layoutManager = LinearLayoutManager(requireContext())

            adapter = transactionAdapter

            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }


    // ============================================================
    // TabLayout
    // ============================================================

    private fun setupTabLayout() {

        binding.transactionTabLayout.addOnTabSelectedListener(

            object : TabLayout.OnTabSelectedListener {

                override fun onTabSelected(
                    tab: TabLayout.Tab?
                ) {

                    currentFilter = when (tab?.position) {

                        // ALL
                        0 -> TransactionFilter.ALL

                        // INCOME
                        1 -> TransactionFilter.INCOME

                        // EXPENSE
                        2 -> TransactionFilter.EXPENSE

                        else -> TransactionFilter.ALL
                    }

                    // Load filtered transactions
                    observeTransactions()
                }


                override fun onTabUnselected(
                    tab: TabLayout.Tab?
                ) {
                }


                override fun onTabReselected(
                    tab: TabLayout.Tab?
                ) {
                }
            }
        )
    }


    // ============================================================
    // Observe Transactions
    // ============================================================

    private fun observeTransactions() {

        // Cancel previous collector
        transactionsJob?.cancel()

        transactionsJob = viewLifecycleOwner.lifecycleScope.launch {

            viewModel
                .getTransactions(currentFilter)
                .collect { transactions ->

                    transactionAdapter.submitList(transactions)
                }
        }
    }

    private fun showTransectiondetails(transectin: Transection){
        //implete mat later
    }
    // ============================================================
    // Destroy View
    // ============================================================

    override fun onDestroyView() {

        transactionsJob?.cancel()
        transactionsJob = null

        super.onDestroyView()

        _binding = null
    }
}