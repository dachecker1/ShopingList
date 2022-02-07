package com.vk.dachecker.shopinglist.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import com.vk.dachecker.shopinglist.R
import com.vk.dachecker.shopinglist.databinding.NewListDialogBinding


object NewListDialog {
    fun showDialog(context: Context, listener: Listener, name: String) {
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        val binding = NewListDialogBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)
        binding.apply {
            edNewListName.setText(name)
            if(name.isNotEmpty()){
                bCreate.text = context.getString(R.string.update)
                tvTitle.text = context.getString(R.string.change_title)
            }
            bCreate.setOnClickListener {
                Toast.makeText(context, "Нажатие сработало", Toast.LENGTH_LONG).show()
                val listName = edNewListName.text.toString()
                if (listName.isNotEmpty()) {
                    listener.onClick(listName)
                    dialog?.dismiss()
                }
            }
        }
        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(null)
        dialog.show()
    }

    interface Listener {
        fun onClick(name: String)
    }
}