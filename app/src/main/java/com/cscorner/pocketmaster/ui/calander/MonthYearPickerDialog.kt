package com.example.pocketmaster.ui.calander

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.cscorner.pocketmaster.R
import com.cscorner.pocketmaster.databinding.FragmentMonthYearPickerDialogBinding
import java.util.Calendar

class MonthYearPickerDialog : DialogFragment() {

    private var _binding: FragmentMonthYearPickerDialogBinding? = null
    private val binding
        get() = _binding!!

    private var onDateSelected: ((Int, Int) -> Unit)? = null
    private var onReset: (() -> Unit)? = null

    // ==========================================
    // Date Selected Listener
    // ==========================================

    fun setOnDateSelectedListener(
        listener: (year: Int, month: Int) -> Unit
    ) {
        onDateSelected = listener
    }

    // ==========================================
    // Reset Listener
    // ==========================================

    fun setOnResetListener(
        listener: () -> Unit
    ) {
        onReset = listener
    }

    // ==========================================
    // Create Dialog
    // ==========================================

    override fun onCreateDialog(
        savedInstanceState: Bundle?
    ): Dialog {

        _binding =
            FragmentMonthYearPickerDialogBinding.inflate(
                LayoutInflater.from(requireContext())
            )

        val calendar = Calendar.getInstance()

        // ==========================================
        // DatePicker Initial Date
        // ==========================================

        binding.datePicker.init(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ) { _, _, _, _ ->
            // Date changed
        }

        // ==========================================
        // Hide Day Picker
        // ==========================================

        val dayId = resources.getIdentifier(
            "day",
            "id",
            "android"
        )

        binding.datePicker
            .findViewById<View>(dayId)
            ?.visibility = View.GONE

        // ==========================================
        // Reset Button
        // ==========================================

        binding.btnReset.setOnClickListener {

            onReset?.invoke()

            dismiss()
        }

        // ==========================================
        // OK Button
        // ==========================================

        binding.btnOk.setOnClickListener {

            val selectedYear =
                binding.datePicker.year

            val selectedMonth =
                binding.datePicker.month

            onDateSelected?.invoke(
                selectedYear,
                selectedMonth
            )

            dismiss()
        }

        // ==========================================
        // Cancel Button
        // ==========================================

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        // ==========================================
        // Create Dialog
        // ==========================================

        return Dialog(requireContext()).apply {

            setContentView(binding.root)

            window?.setBackgroundDrawable(
                ColorDrawable(Color.TRANSPARENT)
            )
        }
    }

    // ==========================================
    // Dialog Size & Animation
    // ==========================================

    override fun onStart() {
        super.onStart()

        dialog?.window?.apply {

            val width =
                (280 * resources.displayMetrics.density).toInt()

            setLayout(
                width,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            setBackgroundDrawable(
                ColorDrawable(Color.TRANSPARENT)
            )

            setWindowAnimations(
                R.style.DialogAnimation
            )
        }
    }

    // ==========================================
    // Clear Binding
    // ==========================================

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    companion object {
        const val TAG = "MonthYearPickerDialog"
    }
}