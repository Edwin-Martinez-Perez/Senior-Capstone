package com.example.collectivetrek.database

data class Album(
    val pictures: List<String>? = null
)
/*
// firebase db will look like this.
// this Album data class object will be saved under album id "album id 1", "album id 2", ...
// album id is child of Albums db and is unique random
"Albums" : {
	"album id 1" : {
		"pictures" : {
			"picture 1" : "picture 1 file name",
			"picture 2" : "picture 2 file name",
			"picture 3" : "picture 3 file name"
		}
	},
	"album id 2" : {
		"pictures" : {
		}
	},
	"album id 3" : {
		"pictures" : {
		}
	},

}
 */
