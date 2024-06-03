package com.example.messenger

data class User(
    val id: String,
    val name: String,
    val lastname: String,
    val age: Int,
    val online: Boolean
 ){
    constructor() : this("", "", "", 0, false){
    }
}