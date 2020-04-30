package cz.muni.fi.pv239.gtodolist.api

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import cz.muni.fi.pv239.gtodolist.model.AppDatabase
import cz.muni.fi.pv239.gtodolist.model.Category
import cz.muni.fi.pv239.gtodolist.model.ToDo
import cz.muni.fi.pv239.gtodolist.model.ToDoRepository
import cz.muni.fi.pv239.gtodolist.ui.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

class ToDoViewModel(application : Application) : AndroidViewModel(application){
    private val repository: ToDoRepository

    val allTodos: LiveData<List<ToDo>>
    val allNotDone: LiveData<List<ToDo>>

    private val TAG = ToDoViewModel::class.java.simpleName

    init{
        val todos = AppDatabase.getDatabase(application, viewModelScope).todoDao()
        repository = ToDoRepository(todos)
        allTodos = repository.allTodos
        allNotDone = repository.allNotDone
        Log.d(TAG, "GOT TODOS FROM DATABASE")
    }

    fun getSortedByCategory(): List<ToDo>{
        var res = ArrayList<ToDo>()
        for(cat in Category.values()){
            for(todo in allNotDone.value!!){
                if(todo.category == cat.toString()){
                    res.add(todo)
                }
            }
        }
        return res.toList()
    }

    fun getTodosOfCategory(cat: String): List<ToDo>{
        val res = ArrayList<ToDo>()
        for(todo in allNotDone.value!!){
            if(todo.category == cat || cat == "ALL"){
                res.add(todo)
            }
        }
        return res.toList()
    }


    fun getTodoWithId(id: Long): ToDo{
        for(todo in allTodos.value!!){
            if(todo.id == id){
                return todo
            }
        }
        return ToDo(0,"Err", "Not found")
    }
    // launching new coroutine to insert data in a non blocking way
    fun insert(todo: ToDo)= viewModelScope.launch(Dispatchers.IO){
        repository.insert(todo)
    }

    // launching new caroutine to update data in a non blocking way
    fun update(todo: ToDo) = viewModelScope.launch(Dispatchers.IO){
        repository.update(todo)
    }




}