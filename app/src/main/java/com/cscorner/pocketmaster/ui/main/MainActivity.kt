package com.cscorner.pocketmaster.ui.main

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.cscorner.pocketmaster.R
import com.cscorner.pocketmaster.databinding.ActivityMainBinding
import com.cscorner.pocketmaster.ui.viewmodel.FinanceViewModel
import com.example.pocketmaster.ui.dialogs.AddTransactionDialog
import com.example.pocketmaster.ui.main.FinancePagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: FinanceViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[FinanceViewModel::class.java]

        setupViewPager()
        setupFab()
    }
    private fun setupViewPager() {
        val adapter = FinancePagerAdapter(this)
        binding.viewPager.adapter = adapter
//        binding.viewPager.registerOnPageChangeCallback(pageChangeCallback)
//        binding.tabLayout.apply {
//            setSelectedTabIndicatorColor(getColor(R.color.white))
//            setTabTextColors(
//                getColor(R.color.divider1), // Unselected color
//                getColor(R.color.white)  // Selected color
//            )
//
//            // Optional: Custom indicator animation
//            setSelectedTabIndicator(R.drawable.tab_indicator) // Create this drawable
//        }
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.tab_dashboard)
                1 -> getString(R.string.tab_transactions)
                else -> getString(R.string.tab_categories)
            }
        }.attach()
    }
    private fun setupFab() {
        binding.fabAddTransaction.setOnClickListener {
            showAddTransactionDialog()
        }
        binding.fabAddTransaction.visibility = when (binding.viewPager.currentItem) {
            0 -> android.view.View.VISIBLE
            else -> android.view.View.GONE
        }
    }

    private fun showAddTransactionDialog() {
        AddTransactionDialog().show(supportFragmentManager, "AddTransaction")
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        // Unregister the stored callback
//        binding.viewPager.unregisterOnPageChangeCallback(pageChangeCallback)
//    }

}