package com.example.frontendproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.frontendproject.databinding.ActivityAiChatBinding

class AiChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityAiChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}