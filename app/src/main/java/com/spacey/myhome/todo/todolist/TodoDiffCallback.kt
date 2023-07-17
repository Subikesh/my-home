package com.spacey.myhome.todo.todolist

import androidx.recyclerview.widget.DiffUtil
import com.spacey.myhome.data.todo.TodoEntity

class TodoDiffCallback(
    private val oldTodoList: List<TodoEntity>,
    private val newTodoList: List<TodoEntity>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldTodoList.size

    override fun getNewListSize(): Int = newTodoList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldTodoList[oldItemPosition].title == newTodoList[newItemPosition].title

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldTodoList[oldItemPosition].isCompleted == newTodoList[newItemPosition].isCompleted

}