package org.d3if1062.mobpro2.tryactionmodeanddialog

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import org.d3if1062.mobpro2.tryactionmodeanddialog.db.Mahasiswa
import org.d3if1062.mobpro2.tryactionmodeanddialog.db.MahasiswaDb
import org.d3if1062.mobpro2.tryactionmodeanddialog.viewmodel.MainViewModel
import org.d3if1062.mobpro2.tryactionmodeanddialog.viewmodel.MainViewModelFactory

class MainActivity : AppCompatActivity(), MainDialog.DialogListener {

    private lateinit var adapter: MainAdapter

    private var actionMode: ActionMode? = null
    private val actionModeCallback = object : ActionMode.Callback {
        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?):
                Boolean {
            if (item?.itemId == R.id.menu_delete) {
                deleteData()
                return true
            }
            if (item?.itemId == R.id.menu_edit) {
                val id = adapter.getSelection()[0]
                viewModel.getData(id)
                Log.d("MainActiviity", "Edit item " + adapter.getSelection())
                updateData()
                return true
            }
            return false
        }

        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?):
                Boolean {
            mode?.menuInflater?.inflate(R.menu.edit_menu, menu)
            mode?.menuInflater?.inflate(R.menu.delete_menu, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?):
                Boolean {
            mode?.title = adapter.getSelection().size.toString()
            if (adapter.getSelection().size != 1) {
                menu?.getItem(0)!!.setVisible(false)
            } else {
                menu?.getItem(0)!!.setVisible(true)
            }
            return true
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            actionMode = null
            adapter.resetSelection()

        }
    }

    private val handler = object : MainAdapter.ClickHandler {
        override fun onLongClick(position: Int): Boolean {
            if (actionMode != null) return false

            adapter.toggleSelection(position)
            actionMode = startSupportActionMode(actionModeCallback)
            return true
        }

        override fun onClick(position: Int, mahasiswa: Mahasiswa) {
            if (actionMode != null) {
                adapter.toggleSelection(position)
                if (adapter.getSelection().isEmpty()) {
                    actionMode?.finish()
                } else {
                    actionMode?.invalidate()
                }
                return
            }

            val message = getString(R.string.mahasiswa_klik, mahasiswa.nama)
            Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
        }
    }

    private fun deleteData() {
        val builder = AlertDialog.Builder(this)
            .setMessage(R.string.pesan_hapus)
            .setPositiveButton(R.string.hapus) { _, _ ->
                viewModel.deleteData(adapter.getSelection())
                actionMode?.finish()
            }
            .setNegativeButton(R.string.batal) { dialog, _ ->
                dialog.cancel()
                actionMode?.finish()
            }
        builder.show()
    }

    private fun updateData() {
        viewModel.dataMahasiswa.also {
            MainDialog(it.value?.nim.toString(), it.value?.nama.toString()).show(
                supportFragmentManager,
                "MainDialog"
            )

        }

    }

    private val viewModel: MainViewModel by lazy {
        val dataSouce = MahasiswaDb.getInstance(this).dao
        val factory = MainViewModelFactory(dataSouce)
        ViewModelProvider(this, factory).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener {
            MainDialog("", "").show(supportFragmentManager, "MainDialog")
        }

        adapter = MainAdapter(handler)
        val itemDecor = DividerItemDecoration(this, RecyclerView.VERTICAL)
        recyclerView.addItemDecoration(itemDecor)
        recyclerView.adapter = adapter
        viewModel.data.observe(this, Observer {
            adapter.submitList(it)
            tv_empty.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
        })
    }

    override fun processDialog(mahasiswa: Mahasiswa) {
        viewModel.insertData(mahasiswa)
    }
}