package com.leehendryp.stoneandroidchallenge.core.extensions

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.widget.SearchView

fun View.visible() {
    visibility = VISIBLE
}

fun View.gone() {
    visibility = GONE
}

fun View.vanish(duration: Long = 300) {
    animate()
        .alpha(0f)
        .setDuration(duration)
        .withEndAction { gone() }
        .start()
}

fun View.fadeIn(duration: Long = 300) {
    animate()
        .alpha(1f)
        .setDuration(duration)
        .withStartAction { visible() }
        .start()
}

fun SearchView.doOnQuerySubmit(block: (String) -> Unit) {
    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            query?.let { block(it) }
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean = false
    })
}
