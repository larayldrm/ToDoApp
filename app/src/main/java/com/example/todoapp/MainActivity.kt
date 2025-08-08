package com.example.todoapp

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.databinding.ActivityMainBinding
import com.example.todoapp.ui.CalendarFragment
import com.example.todoapp.ui.TodoAdapter
import com.example.todoapp.viewmodel.TodoViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: TodoViewModel
    private lateinit var adapter: TodoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ViewModel + Adapter
        viewModel = ViewModelProvider(this)[TodoViewModel::class.java]
        adapter = TodoAdapter(
            onToggleDone = { viewModel.toggleDone(it) },
            onDelte = { viewModel.deleteTodo(it) },
            onEdit = { todo ->
                val editText = EditText(this).apply { setText(todo.title) }
                AlertDialog.Builder(this)
                    .setTitle("Edit Task")
                    .setView(editText)
                    .setPositiveButton("Update") { _, _ ->
                        val newText = editText.text.toString()
                        if (newText.isNotBlank()) {
                            viewModel.updateTodo(todo.copy(title = newText, isDone = false))
                        }
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
        )

        // List
        binding.rvTodos.layoutManager = LinearLayoutManager(this)
        binding.rvTodos.adapter = adapter

        // Add button
        binding.btnAdd.setOnClickListener {
            val text = binding.etTodo.editText?.text.toString()
            if (text.isNotBlank()) {
                viewModel.insertTodo(text)
                binding.etTodo.editText?.text?.clear()
            }
        }

        viewModel.todos.observe(this) { todos ->
            adapter.submitList(todos)
        }

        // Bottom Nav
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_calendar -> {
                    showFragment(CalendarFragment())
                    true
                }
                R.id.nav_tasks -> {
                    hideFragmentArea()
                    true
                }
                R.id.nav_profile -> {
                    // Profil için ileride fragment ekleyeceksin
                    true
                }
                else -> false
            }
        }

        if (savedInstanceState == null) {
            binding.bottomNavigationView.selectedItemId = R.id.nav_tasks
            hideFragmentArea()
        }

        onBackPressedDispatcher.addCallback(this) {
            if (binding.fragmentContainer.isVisible) {
                binding.bottomNavigationView.selectedItemId = R.id.nav_tasks
            } else {
                isEnabled = false
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun showFragment(fragment: androidx.fragment.app.Fragment) {
        binding.tasksRoot.visibility = View.GONE
        binding.fragmentContainer.visibility = View.VISIBLE
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun hideFragmentArea() {
        binding.fragmentContainer.visibility = View.GONE
        binding.tasksRoot.visibility = View.VISIBLE
    }
}
