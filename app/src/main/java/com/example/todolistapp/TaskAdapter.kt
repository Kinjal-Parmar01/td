package com.example.todolistapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private var tasks: ArrayList<Task>,
    private val onDelete: (Task) -> Unit,
    private val onComplete: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val taskTextView: TextView = view.findViewById(R.id.task_text)
        val deleteButton: ImageButton = view.findViewById(R.id.btn_delete)
        val completeCheckBox: CheckBox = view.findViewById(R.id.chk_completed)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.taskTextView.text = task.task
        holder.completeCheckBox.isChecked = task.isCompleted

        holder.completeCheckBox.setOnCheckedChangeListener { _, _ ->
            onComplete(task)
        }

        holder.deleteButton.setOnClickListener {
            onDelete(task)
        }
    }

    override fun getItemCount(): Int = tasks.size

    fun updateList(newList: ArrayList<Task>) {
        tasks = newList
        notifyDataSetChanged()
    }
}