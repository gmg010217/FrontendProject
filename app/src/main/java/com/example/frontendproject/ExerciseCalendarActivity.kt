package com.example.frontendproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.frontendproject.databinding.ActivityExerciseCalendarBinding

class ExerciseCalendarActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityExerciseCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}