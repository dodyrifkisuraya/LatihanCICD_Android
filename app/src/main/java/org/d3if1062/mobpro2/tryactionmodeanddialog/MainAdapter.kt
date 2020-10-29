package org.d3if1062.mobpro2.tryactionmodeanddialog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_main.view.*
import org.d3if1062.mobpro2.tryactionmodeanddialog.db.Mahasiswa

class MainAdapter(private val handler: ClickHandler) :
    ListAdapter<Mahasiswa, MainAdapter.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Mahasiswa>() {
            override fun areItemsTheSame(oldItem: Mahasiswa, newItem: Mahasiswa): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Mahasiswa, newItem: Mahasiswa): Boolean {
                return oldItem == newItem
            }

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item_main, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainAdapter.ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    //handle long klik item
    interface ClickHandler {
        fun onClick(position: Int, mahasiswa: Mahasiswa)
        fun onLongClick(position: Int): Boolean
    }

    private val selectionIds = ArrayList<Int>()
    fun toggleSelection(pos: Int) {
        val id = getItem(pos).id
        if (selectionIds.contains(id))
            selectionIds.remove(id)
        else
            selectionIds.add(id)
        notifyDataSetChanged()
    }

    fun getSelection(): List<Int> {
        return selectionIds
    }

    fun resetSelection() {
        selectionIds.clear()
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(mahasiswa: Mahasiswa) {
            itemView.tv_nim.text = mahasiswa.nim
            itemView.tv_nama.text = mahasiswa.nama
            itemView.setOnLongClickListener { handler.onLongClick(adapterPosition) }
            itemView.isSelected = selectionIds.contains(mahasiswa.id)
            itemView.setOnClickListener { handler.onClick(adapterPosition, mahasiswa) }
        }
    }
}