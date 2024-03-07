package com.example.collectivetrek.database

data class Group(
    val name:String? = null,
    val members:List<String>? = null,
    val startDate: String? = null, //@Theresa, can you save starting and ending date of trip?
    val endDate: String? = null
)
/*
// firebase db will look like this.
// this Group data class object will be saved under group id "group id 1", "group id 2", ...
// group id is child of Groups db and is unique random
"Groups" : {
	"group id 1" : {
		"group_name" : "group name1"
		"members" : {
			"member1" : True,
			"member2" : True,
			"member3" : True,
			"member4" : True
		},
		"startDate": "date",
		"endDate": "date"
	},
	"group id 2" : {
		"group_name" : "group name2"
		"members" : {
			"member5" : True,
			"member6" : True,
			"member7" : True,
			"member8" : True
		},
		"startDate": "date",
		"endDate": "date"
	}

}
 */