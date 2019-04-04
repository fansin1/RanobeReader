package com.fansin.ranobereader.activities

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import com.fansin.ranobereader.R

@SuppressLint("Registered")
open class SearchActivity() : AppCompatActivity() {

    lateinit var searchView : SearchView
    lateinit var searchItem: MenuItem
    lateinit var onQueryTextListener: SearchView.OnQueryTextListener

    private fun View.showKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.options_menu, menu)

        searchItem = menu.findItem(R.id.search)
        searchView = searchItem.actionView as SearchView
        searchView.onActionViewExpanded()

        searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                searchView.showKeyboard()
            }
            else {
                searchView.hideKeyboard()
            }
        }

        searchItem.setOnActionExpandListener(object: MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                searchView.isIconified = false
                searchView.requestFocusFromTouch()
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                // when back, clear all search
                searchView.setQuery("", true)
                return true
            }
        })

        searchView.setOnQueryTextListener(onQueryTextListener)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }

        return true
    }
}