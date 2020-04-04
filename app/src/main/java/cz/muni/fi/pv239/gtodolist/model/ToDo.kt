package cz.muni.fi.pv239.gtodolist.model

data class ToDo (val id:Int, val name: String, val description: String) {

    var done: Boolean = false

    constructor(id:Int, name: String, description: String, done: Boolean) : this(id, name, description) {
        this.done = done
    }
}