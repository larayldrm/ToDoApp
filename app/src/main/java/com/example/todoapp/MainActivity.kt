package com.example.todoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.databinding.ActivityMainBinding
import com.example.todoapp.ui.TodoAdapter
import com.example.todoapp.viewmodel.TodoViewModel
import android.widget.EditText
import androidx.appcompat.app.AlertDialog



class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: TodoViewModel
    private lateinit var adapter: TodoAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel= ViewModelProvider(this)[TodoViewModel::class.java]

        adapter = TodoAdapter(
            onToggleDone = { viewModel.toggleDone(it) },
            onDelte = { viewModel.deleteTodo(it) },
            onEdit = { todo ->
                val editText = EditText(this)
                editText.setText(todo.title)

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


        binding.rvTodos.adapter=adapter
        binding.rvTodos.layoutManager=LinearLayoutManager(this)

        binding.btnAdd.setOnClickListener{
            val text=binding.etTodo.editText?.text.toString()
            if(text.isNotBlank()){
                viewModel.insertTodo(text)
                binding.etTodo.editText?.text?.clear()
            }
        }
        viewModel.todos.observe(this){todos->
            adapter.submitList(todos)
        }
    }
}