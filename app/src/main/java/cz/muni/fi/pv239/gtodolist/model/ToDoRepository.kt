package cz.muni.fi.pv239.gtodolist.model

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class ToDoRepository(private val todoDao: ToDoDao) {
    val allTodos: LiveData<List<ToDo>> = todoDao.findAll()      // Takhle se to zavola hned pri vytvoreni instance tridy. Doporucoval bych getter nebo lazy.
    val allNotDone: LiveData<List<ToDo>> = todoDao.findByState(false)

    @Suppress
    @WorkerThread
    fun insert(todo: ToDo){
        todoDao.insertAll(todo)
    }

    @Suppress
    @WorkerThread
    fun update(todo: ToDo){
        todoDao.updateTodo(todo)
    }

}