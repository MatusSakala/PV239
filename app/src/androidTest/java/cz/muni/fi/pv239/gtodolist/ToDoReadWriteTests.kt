package cz.muni.fi.pv239.gtodolist

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import cz.muni.fi.pv239.gtodolist.model.AppDatabase
import cz.muni.fi.pv239.gtodolist.model.ToDo
import cz.muni.fi.pv239.gtodolist.model.ToDoDao
import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException
import java.util.logging.Logger

@RunWith(AndroidJUnit4::class)
class ToDoReadWriteTests {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var todoDao: ToDoDao
    private lateinit var db: AppDatabase

    @Before
    fun createDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        todoDao = db.TodoDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDatabase() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun findByName() {
        val todo: ToDo = ToDo("Laundry", "DO YOUR LAUNDRY")
        todoDao.insertAll(todo)
        val todoItem = todoDao.findByName(todo.name)
        Assert.assertEquals(1, todoItem.size)
        Assert.assertEquals(todo, todoItem.get(0))
    }

    @Test
    @Throws(Exception::class)
    fun findAll(){
        val todo1 = ToDo("Homework", "DO YOUR HOMEWORK")
        val todo2 = ToDo("DISHES", "DO YOUR DISHES")
        todoDao.insertAll(todo1, todo2)
        val todoItems = todoDao.findAll()
        Assert.assertEquals(2, todoItems.size)
        Assert.assertEquals(todo1, todoItems.get(0))
        Assert.assertEquals(todo2, todoItems.get(1))
        Assert.assertNotSame(todoItems.get(0).id, todoItems.get(1).id)
    }

    @Test
    @Throws(Exception::class)
    fun findByState(){
        val todo1 = ToDo("Homework", "DO YOUR HOMEWORK", true)
        val todo2 = ToDo("DISHES", "DO YOUR DISHES", false)
        todoDao.insertAll(todo1, todo2)
        val doneItems = todoDao.findByState(true)
        val notDoneItems = todoDao.findByState(false)
        Assert.assertEquals(todo1, doneItems.get(0))
        Assert.assertEquals(todo2, notDoneItems.get(0))
        Assert.assertNotSame(doneItems.get(0), notDoneItems.get(0))
    }

    @Test
    @Throws(Exception::class)
    fun testDelete(){
        val todo1 = ToDo("Homework", "DO YOUR HOMEWORK", true)
        todoDao.insertAll(todo1)
        val todoList = todoDao.findAll()
        Assert.assertEquals(1, todoList.size)
        todoDao.delete(todoList.get(0))
        val listAfterDelete = todoDao.findAll()
        Assert.assertEquals(0, listAfterDelete.size)
    }

    @Test
    @Throws(Exception::class)
    fun testUpdate(){
        val todo1 = ToDo("Homework", "DO YOUR HOMEWORK", true)
    }

}