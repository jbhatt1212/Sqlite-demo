package com.example.sqlitedemo

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DataAdapter(
    private val dataItem: MutableList<ModelData>,
    private val onDelete: (Int) -> Unit,
    private val onEdit: (ModelData) -> Unit
) : RecyclerView.Adapter<DataAdapter.DataViewHolder>() {
    class DataViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tvName)
        val age: TextView = view.findViewById(R.id.tvAge)
        val btnDelete: TextView = view.findViewById(R.id.btnDelete)
        val btnEdit: TextView = view.findViewById(R.id.btnEdit)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.data_items,parent,false)
        return DataViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
       val data = dataItem[position]
        holder.name.text = data.name
        holder.age.text = data.age
        holder.btnDelete.setOnClickListener { onDelete(data.id) }
        holder.btnEdit.setOnClickListener { onEdit(data) }
    }

    override fun getItemCount(): Int {
        return dataItem.size
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData :List<ModelData>){
        dataItem.clear()
        dataItem.addAll(newData)
        notifyDataSetChanged()
    }

}