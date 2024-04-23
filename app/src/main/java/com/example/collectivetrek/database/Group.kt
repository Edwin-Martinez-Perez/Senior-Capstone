package com.example.collectivetrek.database

import java.util.Date

data class Group(
    val groupName:String? = null,
    val date: String? = null, //@Theresa, can you save starting and ending date of trip?
    val destination: String? = null
)