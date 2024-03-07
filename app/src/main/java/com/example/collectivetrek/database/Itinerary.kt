package com.example.collectivetrek.database

data class Itinerary(
    val filters: List<String>? = null // this list holds filter IDs for specific group
)

// jso looks like below
/*
"Itinerary" : {
    // this data class object belongs to each "group id 1", "group id 2",...
	"group id 1" : {
		"filters":{
			"filter id 1" : True,
			"filter id 2" : True,
			"filter id 3" : True
		}
	},
	"group id 2" : {
		"filters":{
			"filter id 4" : True,
			"filter id 5" : True,
			"filter id 6" : True
		}
	}
*/