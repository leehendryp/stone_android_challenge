package com.leehendryp.stoneandroidchallenge.core.extensions

import androidx.appcompat.widget.SearchView

fun SearchView.doOnQuerySubmit(block: (String) -> Unit) {
    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            query?.let { block(it) }
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean = false
    })
}