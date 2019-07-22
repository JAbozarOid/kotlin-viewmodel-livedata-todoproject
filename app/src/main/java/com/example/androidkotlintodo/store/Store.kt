package com.example.androidkotlintodo.store

import com.example.androidkotlintodo.model.Action
import androidx.arch.core.util.Function

interface Store<T> {
    fun dispatch(action: Action)

    // with this method we can subscribe our activity to our livedata and allows us to transform our livedata in different maner
    fun subscribe(renderer: Renderer<T>,func: Function<T,T> = Function { it })
}