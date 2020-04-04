package cz.muni.fi.pv239.gtodolist.api

import cz.muni.fi.pv239.gtodolist.model.ToDo


class ToDoService {

    val mockToDoList = listOf(
        ToDo(1, "prve TODO", "volitelny dobrovolny popis" ),
        ToDo(2, "druhe TODO", "volitelny dobrovolny popis" ),
        ToDo(3, "tretie TODO", "volitelny dobrovolny popis" ),
        ToDo(4, "stvrte TODO", "volitelny dobrovolny popis" ),
        ToDo(5, "piate TODO", "volitelny dobrovolny popis" ),
        ToDo(6, "sieste TODO", "volitelny dobrovolny popis" ),
        ToDo(7, "siedme TODO", "volitelny dobrovolny popis" ),
        ToDo(8, "osme TODO aaaaaaa", "volitelny dobrovolny popis" ),
        ToDo(9, "deviate TODO", "volitelny dobrovolny popis" ),
        ToDo(10, "desiate TODO", "volitelny dobrovolny popis" ),
        ToDo(11, "11 TODO", "volitelny dobrovolny popis"),
        ToDo(12, "12 TODO", "volitelny dobrovolny popis" ),
        ToDo(13, "13 TODO", "volitelny dobrovolny popis"),
        ToDo(14, "14 TODO", "volitelny dobrovolny popis" ),
        ToDo(15, "15 TODO", "volitelny dobrovolny popis" )
    )

    fun listAll(): List<ToDo> {
        return mockToDoList
    }
}