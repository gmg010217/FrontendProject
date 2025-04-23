package com.example.frontendproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.frontendproject.databinding.ActivityFreeBoardBinding
import com.example.frontendproject.fragment.FreeBoardListFragment

class FreeBoardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityFreeBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.freeBoardToolbar)
        supportActionBar?.title = "자유 게시판"

        supportFragmentManager.beginTransaction()
            .replace(R.id.freeBoardFragmentContainer, FreeBoardListFragment())
            .commit()
    }
}