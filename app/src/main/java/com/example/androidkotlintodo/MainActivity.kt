package com.example.androidkotlintodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.arch.core.util.Function
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.androidkotlintodo.model.*
import com.example.androidkotlintodo.store.Renderer
import com.example.androidkotlintodo.store.TodoStore
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.selector

class MainActivity : AppCompatActivity(), Renderer<TodoModel> {

    private lateinit var store: TodoStore

    override fun render(model: LiveData<TodoModel>) {
        model.observe(this, Observer { newState ->
            listView.adapter = TodoAdapter(this, newState?.todos ?: listOf())
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // to get our data out of our to do store we want to use ViewModel library
        store = ViewModelProviders.of(this).get(TodoStore::class.java)
        store.subscribe(this, mapStateToProps)

        addButton.setOnClickListener {
            store.dispatch(AddTodo(editText.text.toString()))
            editText.text = null
        }
        fab.setOnClickListener { openDialog() }

        listView.adapter = TodoAdapter(this, listOf())
        listView.setOnItemClickListener { _, _, _, id ->
            store.dispatch(ToggleTodo(id))
        }
        listView.setOnItemLongClickListener { _, _, _, id ->
            store.dispatch(RemoveTodo(id))
            true
        }

    }

    private fun openDialog() {
        val options = resources.getStringArray(R.array.filter_options).asList()
        // selector is a method in Ankle Library
        selector(getString(R.string.filter_title), options) { _, i ->
            val visible = when (i) {
                1 -> Visibility.Active()
                2 -> Visibility.Completed()
                else -> Visibility.All()
            }
            store.dispatch(SetVisibility(visible))
        }
    }

    // filter to dos
    private val mapStateToProps = Function<TodoModel, TodoModel> {
        val keep: (Todo) -> Boolean = when (it.visibility) {
            is Visibility.All -> { _ -> true }
            is Visibility.Active -> { t: Todo -> !t.status }
            is Visibility.Completed -> { t: Todo -> t.status }
        }
        return@Function it.copy(todos = it.todos.filter { keep(it) })
    }
}
