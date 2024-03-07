package com.example.collectivetrek.database

data class Gallery(
    val album: List<String>? = null
)
/*
// firebase db will look like this.
// this Gallery data class object will be saved under group id "group id 1", "group id 2", ...
// group id is child of Galleries db and is unique random
"Galleries" : {
	"group id 1" : {
		"albums" : {
			"album id 1" : True,
			"album id 2" : True,
			"album id 3" : True
		}
	}
}
 */
