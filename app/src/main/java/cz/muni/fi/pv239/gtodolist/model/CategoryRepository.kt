package cz.muni.fi.pv239.gtodolist.model

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class CategoryRepository(private val categoryDao:CategoryDao) {
    val allCategories: LiveData<List<Category>> = categoryDao.findAll()

    @Suppress
    @WorkerThread
    fun insert(category:Category){
        categoryDao.insertAll(category)
    }

    @Suppress
    @WorkerThread
    fun update(category: Category){
        categoryDao.updateCategory(category)
    }
}