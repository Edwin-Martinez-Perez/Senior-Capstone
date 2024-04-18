package com.example.collectivetrek.database

import android.graphics.Bitmap
import java.net.URL

data class Event(
    val eventId: String? = null,
    val placeName: String? = null,
    val date: String? = null,
    val pinNum: Int? = null,
    var coordinates: String? = null,
    var address: String? = null,
    val note: String? = null,
    var bitmap: String? = null
)

/*
// firebase db will look like this.
// this Event data class object will be saved under event id "event id 1", "event id 2", ...
// event id is child of Events db and is unique random
// when the event is not a place (such as transportation, or user didn't enter an address), pin/coordinates/address can be null
"Events" : {
	"event id 1" : {
		"place_name" : "first place name",
		"date" : "",
		"pin_number" : "1", // how to manage pin number? keep update?
		"coordinates" : "41.7730802626321, -88.14378174906348",
		"address" : "address Naperville IL 60540",
		"filter_id" : "filter name1"
	},
	"event id 2" : {
		"place_name" : "second place name",
		"date" : "",
		"pin_number" : "2",
		"coordinates" : "41.7730802626321, -88.14378174906348",
		"address" : "address Naperville IL 60540",
		"filter_name" : "filter name2"
	},
	"event id 3" : {
		"place_name" : "third place name",
		"date" : "",
		"pin_number" : null,
		"coordinates" : null,
		"address" : null,
		"filter_name" : "filter name3"
	}
}
 */
