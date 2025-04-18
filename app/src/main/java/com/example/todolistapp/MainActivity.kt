package com.example.todolistapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: TaskDatabaseHelper
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var taskList: ArrayList<Task>

    private lateinit var inputTask: EditText
    private lateinit var addButton: Button
    private lateinit var searchBox: EditText
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        inputTask = findViewById(R.id.input_task)
        addButton = findViewById(R.id.btn_add)
        searchBox = findViewById(R.id.search_box)
        recyclerView = findViewById(R.id.recyclerView)

        // Initialize DB helper and task list
        dbHelper = TaskDatabaseHelper(this)
        taskList = dbHelper.getAllTasks()

        // Setup adapter and recycler view
        taskAdapter = TaskAdapter(
            taskList,
            onDelete = { task ->
                dbHelper.deleteTask(task.id)
                refreshList()
            },
            onComplete = { task ->
                dbHelper.markTaskCompleted(task.id)
                refreshList()
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = taskAdapter

        // Add task button
        addButton.setOnClickListener {
            val taskText = inputTask.text.toString().trim()
            if (taskText.isNotEmpty()) {
                dbHelper.insertTask(taskText)
                inputTask.text.clear()
                refreshList()
            } else {
                Toast.makeText(this, "Please enter a task", Toast.LENGTH_SHORT).show()
            }
        }

        // Search tasks
        searchBox.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterList(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    // Refresh task list from database
    private fun refreshList() {
        taskList = dbHelper.getAllTasks()
        taskAdapter.updateList(taskList)
    }

    // Filter tasks by search query
    private fun filterList(query: String) {
        val filtered = taskList.filter { it.task.contains(query, ignoreCase = true) } as ArrayList<Task>
        taskAdapter.updateList(filtered)
    }
}