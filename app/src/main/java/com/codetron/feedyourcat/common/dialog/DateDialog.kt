package com.codetron.feedyourcat.common.dialog

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

typealias DateResult = (year: Int, month: Int, day: Int) -> Unit

class DateDialog(
    private val defaultYear: Int? = null,
    private val defaultMonth: Int? = null,
    private val defaultDay: Int? = null,
    private val result: DateResult,
) : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        if (defaultYear != null && defaultMonth != null && defaultDay != null) {
            return DatePickerDialog(
                requireActivity(),
                this,
                defaultYear,
                defaultMonth,
                defaultDay
            ).apply {
                datePicker.maxDate = Date().time
            }
        }

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(requireActivity(), this, year, month, day).apply {
            datePicker.maxDate = Date().time
        }
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        result(year, month, day)
    }

    companion object {
        const val TAG = "DateDialog"
    }

}
