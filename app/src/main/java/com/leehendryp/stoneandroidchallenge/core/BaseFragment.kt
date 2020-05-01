package com.leehendryp.stoneandroidchallenge.core

import android.content.DialogInterface
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.leehendryp.stoneandroidchallenge.R

open class BaseFragment : Fragment() {
    private var dialog: AlertDialog? = null

    fun createAlertDialog(
        @StringRes title: Int? = R.string.error_title,
        @StringRes message: Int? = R.string.error_retry,
        @StringRes positiveButton: Int? = android.R.string.ok,
        positiveListener: DialogInterface.OnClickListener? = null,
        @StringRes neutralButton: Int? = null,
        neutralListener: DialogInterface.OnClickListener? = null,
        @StringRes negativeButton: Int? = null,
        negativeListener: DialogInterface.OnClickListener? = null
    ): AlertDialog? {

        val alertDialog = context?.let { context ->
            AlertDialog.Builder(context).setCancelable(false)
        }

        title?.let { alertDialog?.setTitle(it) }
        message?.let { alertDialog?.setMessage(it) }
        positiveButton?.let { alertDialog?.setPositiveButton(it, positiveListener) }
        neutralButton?.let { alertDialog?.setNeutralButton(it, neutralListener) }
        negativeButton?.let { alertDialog?.setNegativeButton(it, negativeListener) }

        dialog = alertDialog?.create()

        return dialog
    }

    fun showErrorDialog(
        @StringRes title: Int? = null,
        @StringRes message: Int? = null
    ) {
        createAlertDialog(
            title ?: R.string.error_title,
            message ?: R.string.error_retry
        )?.show()
    }

    override fun onDestroy() {
        dialog?.dismiss()
        super.onDestroy()
    }
}