package com.example.frontendproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.frontendproject.databinding.ActivityExerciseCalendarBinding
import com.example.frontendproject.databinding.ActivityExerciseDetailBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityExerciseDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}