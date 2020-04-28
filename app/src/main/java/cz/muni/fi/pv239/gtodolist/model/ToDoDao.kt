package cz.muni.fi.pv239.gtodolist.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ToDoDao {
    @Query("SELECT * FROM todo_items WHERE done LIKE :done")
    fun findByState(done: Boolean): LiveData<List<ToDo>>

    @Query("SELECT * FROM todo_items WHERE name LIKE :name")
    fun findByName(name: String): LiveData<List<ToDo>>

    @Query("SELECT * FROM todo_items")
    fun findAll(): LiveData<List<ToDo>>

    @Insert
    fun insertAll(vararg todo: ToDo)

    @Delete
    fun delete(tofo: ToDo)

    @Query("DELETE FROM todo_items")
    fun deleteAll()

    @Update
    fun updateTodo(vararg todos: ToDo)
}