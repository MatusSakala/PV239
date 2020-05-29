package cz.muni.fi.pv239.gtodolist.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_items")
data class ToDo (
    @PrimaryKey(autoGenerate = true) var id:Long,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "done") var done: Boolean,
    @ColumnInfo(name = "category") var category: Category,
    @ColumnInfo(name = "importance") var importance: Long,
    @ColumnInfo(name = "added") var dateAdded: String)
{

    constructor(name: String, description: String) : this(0, name, description, false, Category("", ""), 0, "")
    constructor(id: Long, name: String, description: String) : this(id, name, description, false, Category("", ""), 0, "")
    constructor(name: String, description: String, done: Boolean) : this(0, name, description, done, Category("", ""), 0, "")
    constructor(name: String, description: String, done: Boolean, category: Category): this(0, name, description, done, category, 0, "")
    constructor(name: String, description: String, done: Boolean, category: Category, importance: Long): this(0, name, description, done, category, importance, "")
    constructor(name: String, description: String, done: Boolean, category: Category, importance: Long, added: String): this(0, name, description, done, category, importance, added)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ToDo

        if (name != other.name) return false
        if (description != other.description) return false
        if (done != other.done) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + done.hashCode()
        return result
    }
}