package com.example.frontendproject

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.frontendproject.databinding.ActivityDiaryBinding
import com.example.frontendproject.fragment.DiaryListFragment

class DiaryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityDiaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val titleTextView = findViewById<TextView>(R.id.diaryTitleText)

        supportFragmentManager.beginTransaction()
            .replace(R.id.diaryFragmentContainer, DiaryListFragment())
            .commit()
    }
}