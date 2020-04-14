package cz.muni.fi.pv239.gtodolist.model

data class ToDo (val id:Long, val name: String, val description: String, var done: Boolean) {

    constructor(id:Long, name: String, description: String) : this(id, name, description, false)

}