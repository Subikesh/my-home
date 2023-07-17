package com.spacey.myhome.todo

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.spacey.myhome.data.todo.TodoEntity
import com.spacey.myhome.databinding.FragmentTodoItemBinding
import com.spacey.myhome.todo.todolist.TodoDiffCallback

class TodoListRecyclerViewAdapter(
    initialTodoList: List<TodoEntity>
) : RecyclerView.Adapter<TodoListRecyclerViewAdapter.ViewHolder>() {

    private val todoList: MutableList<TodoEntity> = initialTodoList.toMutableList()

    fun updateTodoList(newList: List<TodoEntity>) {
        val todoDiffCallback = TodoDiffCallback(todoList, newList)
        val diffResult = DiffUtil.calculateDiff(todoDiffCallback)
        todoList.clear()
        todoList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
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
        holder.idView.text = if (item.isCompleted) "Yes" else "No"
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