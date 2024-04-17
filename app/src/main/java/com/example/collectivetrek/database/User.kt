package com.example.collectivetrek.database

data class User(
    val firstName:String? = null,
    val lastName:String? = null,
    val email:String? = null,
    val password:String? = null,
    val profilePicture: String? = null
)
/*
// firebase db will look like this.
// this User data class object will be saved under user id "user id 1", "user id 2", ...
// user id is child of Users db and is unique random
"Users" : {
	"user id 1" : {
		"username": "first user",
		"password" : "password1",
		"email" : "user1@email.com",
		"profile picture" : "profile picture file"
	},
	"user id 2" : {
		"username": "second user",
		"password" : "password2",
		"email" : "user2@email.com",
		"profile picture" : "profile picture file"
	},
	"user id 3" : {
		"username": "third user",
		"password" : "password3",
		"email" : "user3@email.com",
		"profile picture" : "profile picture file"
	}
}
* */