package cz.muni.fi.pv239.gtodolist.api

import android.app.Application
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import cz.muni.fi.pv239.gtodolist.model.AppDatabase
import cz.muni.fi.pv239.gtodolist.model.Category
import cz.muni.fi.pv239.gtodolist.model.CategoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryViewModel(application : Application): AndroidViewModel(application) {
    private val repository: CategoryRepository

    val userCategories: LiveData<List<Category>>

    private val TAG = CategoryViewModel::class.java.simpleName

    init{
        val categories = AppDatabase.getDatabase(application, viewModelScope).categoryDao()
        repository = CategoryRepository(categories)
        userCategories = repository.allCategories
    }

    fun insert(cat: Category)= viewModelScope.launch(Dispatchers.IO){
        repository.insert(cat)
    }

    fun update(cat: Category)= viewModelScope.launch(Dispatchers.IO){
        repository.update(cat)
    }

    fun findByName(name: String): Category{
        val categories = getAllCategories()
        for(c in categories){
            if(c.name == name){
                return c
            }
        }
        return Category("", "#000000")
    }

    fun getAllCategories(): List<Category>{
        var res = ArrayList<Category>()
        res.add(Category(-1,"None", "#BEBECC"))
        res.add(Category(-2,"Personal", "#CC5252"))
        res.add(Category(-3,"Work", "#52CC52"))
        res.add(Category(-4,"Travel", "#5252CC"))
        //res.addAll(userCategories.value!!)
        for(c in userCategories.value!!){
            res.add(c)
        }
        return res.toList()
    }

}