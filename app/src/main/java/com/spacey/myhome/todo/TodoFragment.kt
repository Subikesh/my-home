package com.spacey.myhome.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.spacey.myhome.R
import kotlinx.coroutines.launch

/**
 * A fragment representing a list of Items.
 */
class TodoFragment : Fragment() {

    private val todoViewModel: TodoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_todo, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            val todoListAdapter = TodoListRecyclerViewAdapter(emptyList())
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = todoListAdapter
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    todoViewModel.todoUIState.collect { todoList ->
                        todoListAdapter.updateTodoList(todoList)
                    }
                }
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar: Toolbar = requireActivity().findViewById(R.id.home_toolbar)
        toolbar.title = "Todo Checklist"

        todoViewModel.fetchTodos()
    }
}