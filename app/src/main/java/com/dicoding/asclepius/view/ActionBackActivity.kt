package com.dicoding.asclepius.view

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

open class AppCompatActivityWithActionBack : AppCompatActivity() {

    protected fun setupAppBar(toolbar: MaterialToolbar, title: String) {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            this.title = title
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

