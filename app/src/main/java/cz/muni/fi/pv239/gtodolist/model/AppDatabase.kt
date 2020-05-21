package cz.muni.fi.pv239.gtodolist.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [ToDo::class, Category::class],
    version = 8
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao(): ToDoDao
    abstract fun categoryDao(): CategoryDao

    companion object{
        @Volatile private var instance: AppDatabase? = null
        private val LOCK = Any()

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): AppDatabase {
            return instance ?: synchronized(this){
                val inst = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "todo_items.db"
                )
                    // Wipes and rebuilds instead of migrating if no Migration object.
                    // Migration is not part of this codelab.
                    .fallbackToDestructiveMigration()
                    .addCallback(WordDatabaseCallback(scope))
                    .build()
                instance = inst
                // return instance
                inst
            }
        }

        private class WordDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                instance?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        //populateDatabase(database.todoDao())
                    }
                }
            }
        }

        fun populateDatabase(todoDao: ToDoDao){
            var todo = ToDo("LAUNDRY", "DO YOUR LAUNDRY")
            todoDao.insertAll(todo)
            todo = ToDo("HOMEWORK", "REALLY!!!")
            todoDao.insertAll(todo)
        }

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            AppDatabase::class.java, "todo_items.db")
            .build()
    }
}