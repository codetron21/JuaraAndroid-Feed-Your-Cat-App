package com.codetron.feedyourcat.common.dialog

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.codetron.feedyourcat.R

class LoadingDialog : DialogFragment(R.layout.dialog_loading) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.setCancelable(false)
        dialog?.window?.setBackgroundDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                android.R.color.transparent
            )
        )
    }
    companion object{
        const val TAG = "LoadingDialog"
    }

}