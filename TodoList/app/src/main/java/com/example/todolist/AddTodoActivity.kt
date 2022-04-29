package com.example.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.todolist.db.AppDatabase
import com.example.todolist.databinding.ActivityAddTodoBinding
class AddTodoActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    lateinit var db : AppDatabase
    lateinit var toDoDao: ToDoDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddTodoBinding.inflate(layoutInflater)

    }
}