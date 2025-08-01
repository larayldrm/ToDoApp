package com.example.todoapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.Todo
import com.example.todoapp.data.TodoDatabase
import kotlinx.coroutines.launch

class TodoViewModel(application: Application): AndroidViewModel(application) {
    private val dao= TodoDatabase.getDatabase(application).todoDao()
    val todos: LiveData<List<Todo>> = dao.getAllTodo()

    fun insertTodo(title: String)= viewModelScope.launch {
        dao.insert(Todo(title=title))
    }
    fun toggleDone(todo: Todo)= viewModelScope.launch{
        dao.update(todo.copy(isDone= !todo.isDone))
    }
    fun deleteTodo(todo:Todo)= viewModelScope.launch {
        dao.delete(todo)
    }
    fun updateTodo(todo: Todo) = viewModelScope.launch {
        dao.update(todo)
    }

}