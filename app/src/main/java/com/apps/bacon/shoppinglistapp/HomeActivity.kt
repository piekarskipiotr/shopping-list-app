package com.apps.bacon.shoppinglistapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.apps.bacon.shoppinglistapp.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initTabs()

    }

    private fun initTabs() {
        val listOfTitles =
            listOf(getString(R.string.shopping_lists), getString(R.string.archived_shopping_lists))
        val listOfIcons = listOf(
            ContextCompat.getDrawable(this@HomeActivity, R.drawable.ic_round_list),
            ContextCompat.getDrawable(this@HomeActivity, R.drawable.ic_round_archive)
        )

        val size = listOfTitles.size - 1
        for (i in 0..size) {
            val isSelected = i == 0
            val tab = binding.tabLayout.newTab().setText(listOfTitles[i]).apply {
                icon = listOfIcons[i]
            }
            binding.tabLayout.addTab(tab, i, isSelected)
        }
    }
}