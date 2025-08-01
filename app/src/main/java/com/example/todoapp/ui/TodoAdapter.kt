package com.example.todoapp.ui

import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.data.Todo
import com.example.todoapp.databinding.ItemTodoBinding


class TodoAdapter (
    private val onToggleDone: (Todo) -> Unit,
    private val onDelte: (Todo) -> Unit,
    private val onEdit: (Todo) -> Unit
) : ListAdapter<Todo, TodoAdapter.TodoViewHolder>(DiffCallBack()){

    class TodoViewHolder(val binding: ItemTodoBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding= ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent,false )
        return TodoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todo = getItem(position)
        with(holder.binding) {
            textviewTitle.text = todo.title


            checkboxDone.setOnCheckedChangeListener(null)

            checkboxDone.isChecked = todo.isDone

            textviewTitle.paintFlags = if (todo.isDone)
                textviewTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            else
                textviewTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()


            checkboxDone.setOnCheckedChangeListener { _, _ -> onToggleDone(todo) }

            buttonDelete.setOnClickListener { onDelte(todo) }
            buttonEdit.setOnClickListener { onEdit(todo) }
        }
    }


    class DiffCallBack: DiffUtil.ItemCallback<Todo>(){
    override fun areItemsTheSame(oldItem: Todo, newItem: Todo)= oldItem.id==newItem.id
    override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
        val result = oldItem.title == newItem.title && oldItem.isDone == newItem.isDone
        Log.d("DiffUtil", "Comparing:\nOld: $oldItem\nNew: $newItem\nResult: $result")
        return result
    }


}
}