package com.example.androidkotlintodo.store

import androidx.lifecycle.LiveData


// render ui for our model
interface Renderer<T> {
    fun render(model: LiveData<T>)
}