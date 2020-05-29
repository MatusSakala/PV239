package cz.muni.fi.pv239.gtodolist.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category_items")
data class Category(
    @PrimaryKey(autoGenerate = true) var id:Long,
    @ColumnInfo(name = "name") var name:String,
    @ColumnInfo(name = "color") var color:String,
    @ColumnInfo(name = "added") var added:String){
    constructor(name:String, color: String) : this(0, name, color, "")
    constructor(id:Long, name:String, color:String): this(id, name, color, "")

}