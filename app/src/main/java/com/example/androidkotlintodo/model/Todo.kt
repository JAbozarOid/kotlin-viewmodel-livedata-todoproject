package com.example.androidkotlintodo.model

// our to do is something like has a text and id and a status which means has completed by user or not
data class Todo(val text: String, val id: Long, val status: Boolean = false)