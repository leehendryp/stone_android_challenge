package com.leehendryp.stoneandroidchallenge.core

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.leehendryp.stoneandroidchallenge.R
import com.leehendryp.stoneandroidchallenge.core.extensions.doOnQuerySubmit
import com.leehendryp.stoneandroidchallenge.databinding.ActivityMainBinding
import com.leehendryp.stoneandroidchallenge.feed.presentation.FreeTextSearcher

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    var freeTextSearcher: FreeTextSearcher? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)

        val searchOption = menu?.findItem(R.id.menu_search)

        searchOption?.let { option ->
            val searchView = option.actionView as SearchView
            searchView.doOnQuerySubmit { query -> freeTextSearcher?.search(query) }
        }

        return super.onCreateOptionsMenu(menu)
    }
}