package com.example.frontendproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.frontendproject.databinding.ActivityExerciseCalendarBinding
import com.example.frontendproject.fragment.ExerciseCalendarFragment

class ExerciseCalendarActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityExerciseCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.exerciseFragmentContainer, ExerciseCalendarFragment())
            .commit()
    }
}