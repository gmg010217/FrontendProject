package com.example.frontendproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.frontendproject.databinding.ActivityJoinBinding
import com.example.frontendproject.fragment.SignupStep1Fragment

class JoinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.joinFragmentContainer, SignupStep1Fragment())
            .commit()
    }
}