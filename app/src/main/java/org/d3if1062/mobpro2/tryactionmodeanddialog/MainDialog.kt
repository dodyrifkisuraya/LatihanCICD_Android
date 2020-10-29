package org.d3if1062.mobpro2.tryactionmodeanddialog

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.dialog_main.view.*
import org.d3if1062.mobpro2.tryactionmodeanddialog.db.Mahasiswa

class MainDialog(val nim: String, val nama: String) : DialogFragment() {

    var nimMhs: String = ""
    var namaMhs: String = ""

    init {
        nimMhs = nim
        namaMhs = nama
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = LayoutInflater.from(requireContext())
        val view = inflater.inflate(R.layout.dialog_main, null, false)
        val builder = AlertDialog.Builder(requireContext())
        with(builder) {
            setTitle(
                if (nim == "") R.string.mahasiswa_baru else R.string.mahasiswa_edit
            )
            setView(view)
            setPositiveButton(R.string.simpan) { _, _ ->
                val mahasiswa = getData(view) ?: return@setPositiveButton
                val listener = requireActivity() as DialogListener
                listener.processDialog(mahasiswa)
            }
            setNegativeButton(R.string.batal) { _, _ -> dismiss() }
        }
        view.nimEditText.setText(nimMhs)
        view.namaEditText.setText(namaMhs)
        return builder.create()
    }

    private fun getData(view: View): Mahasiswa? {
        val nimEditText = view.findViewById<EditText>(R.id.nimEditText)
        val namaEditText = view.findViewById<EditText>(R.id.namaEditText)

        if (nimEditText.text.isEmpty()) {
            showMessage(R.string.nim_wajib_diisi)
            return null
        }
        if (nimEditText.text.length != 10) {
            showMessage(R.string.nim_harus_10chars)
            return null
        }
        if (namaEditText.text.isEmpty()) {
            showMessage(R.string.nama_wajib_diisi)
            return null
        }


        return Mahasiswa(
            nim = nimEditText.text.toString(),
            nama = namaEditText.text.toString()
        )
    }

    private fun showMessage(messageResId: Int) {
        val toast = Toast.makeText(
            requireContext(), messageResId,
            Toast.LENGTH_LONG
        )
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }


    interface DialogListener {
        fun processDialog(mahasiswa: Mahasiswa)
    }

}