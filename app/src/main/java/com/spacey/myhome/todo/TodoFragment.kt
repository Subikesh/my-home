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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.spacey.myhome.R
import kotlinx.coroutines.launch

/**
 * A fragment representing a list of Items.
 */
class TodoFragment : Fragment() {

    private val todoViewModel: TodoViewModel by viewModels()

    private lateinit var todoList: RecyclerView
    private lateinit var todoSwipeLayout: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_todo, container, false)
        val todoListAdapter = TodoListRecyclerViewAdapter(emptyList())

        todoSwipeLayout = view.findViewById(R.id.todo_swipe_layout)
        todoList = view.findViewById<RecyclerView>(R.id.todo_list).apply {
            layoutManager = LinearLayoutManager(context)
            adapter = todoListAdapter
        }

        todoSwipeLayout.setOnRefreshListener {
            todoViewModel.fetchTodos()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                todoViewModel.todoUIState.collect { todoResult ->
                    when (todoResult) {
                        is TodoUIState.Loading -> {
                            todoSwipeLayout.isRefreshing = true
                        }
                        is TodoUIState.Success -> {
                            Snackbar.make(todoSwipeLayout, "TodoResults fetched", Snackbar.LENGTH_SHORT).show()
                            todoListAdapter.updateTodoList(todoResult.data)
                            todoSwipeLayout.isRefreshing = false
                        }
                        is TodoUIState.Error -> {
                            Snackbar.make(todoSwipeLayout, todoResult.errorMessage, Snackbar.LENGTH_INDEFINITE).show()
                            todoSwipeLayout.isRefreshing = false
                        }
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