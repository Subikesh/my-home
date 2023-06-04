package com.spacey.myhome.todo

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.spacey.myhome.databinding.FragmentTodoItemBinding

class TodoListRecyclerViewAdapter(
    initialTodoList: List<TodoUIState>
) : RecyclerView.Adapter<TodoListRecyclerViewAdapter.ViewHolder>() {

    private var todoList: List<TodoUIState> = initialTodoList

    fun updateTodoList(newList: List<TodoUIState>) {
        if (todoList != newList) {
            todoList = newList
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentTodoItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = todoList[position]
        holder.idView.text = if (item.isDone) "Yes" else "No"
        holder.contentView.text = item.title
    }

    override fun getItemCount(): Int = todoList.size

    class ViewHolder(binding: FragmentTodoItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }

}