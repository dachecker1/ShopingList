package com.vk.dachecker.shopinglist.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import com.vk.dachecker.shopinglist.databinding.DeleteDialogBinding
import com.vk.dachecker.shopinglist.databinding.NewListDialogBinding


object DeleteDialog {
    fun showDialog(context: Context, listener: Listener) {
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        val binding = DeleteDialogBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)
        binding.apply {
            bDelete.setOnClickListener {
                listener.onClick()
                dialog?.dismiss()
                Toast.makeText(context, "Нажатие сработало", Toast.LENGTH_LONG).show()
            }

            bCancel.setOnClickListener {
                dialog?.dismiss()
                Toast.makeText(context, "Нажатие сработало", Toast.LENGTH_LONG).show()
            }
        }
        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(null)
        dialog.show()
    }

    interface Listener {
        fun onClick()
    }
}