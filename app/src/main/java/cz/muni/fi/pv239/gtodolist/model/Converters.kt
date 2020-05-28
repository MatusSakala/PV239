package cz.muni.fi.pv239.gtodolist.model

import androidx.room.TypeConverter
import java.util.ArrayList

class Converters {
    @TypeConverter
    fun stringFromCategory(cat:Category): String {
        return cat.toString()
    }

    @TypeConverter
    fun categoryFromString(str:String): Category{
        var slice = str.toString().slice(9..(str.length-2))
        var params = slice.split(", ")
        var values = ArrayList<String>()
        for(p in params){
            var temp = p.split("=")
            values.add(temp[1])
        }
        return Category(values[0].toLong(),values[1],values[2],values[3])
    }

}