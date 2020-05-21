package cz.muni.fi.pv239.gtodolist.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CategoryDao {
    @Query("SELECT * FROM category_items WHERE name LIKE :name")
    fun findByName(name: String): LiveData<List<Category>>

    @Query("SELECT * FROM category_items")
    fun findAll(): LiveData<List<Category>>

    @Insert
    fun insertAll(vararg categories: Category)

    @Delete
    fun delete(category: Category)

    @Query("DELETE FROM category_items")
    fun deleteAll()

    @Update
    fun updateCategory(vararg categories: Category)

}