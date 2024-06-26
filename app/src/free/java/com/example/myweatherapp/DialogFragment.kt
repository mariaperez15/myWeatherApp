package com.example.myweatherapp

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class DialogFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Título del Diálogo")
            .setMessage("!Hazte premium para poder disfrutar de todas las funcionalidades de nuestra App!")
            .setPositiveButton("Me lo pienso") { dialog: DialogInterface, id: Int ->
            }
            .setNegativeButton("No gracias") { dialog: DialogInterface, id: Int ->
            }
        return builder.create()
    }
}
