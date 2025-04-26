package com.example.frontendproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.frontendproject.databinding.ActivityCounselBoardBinding
import com.example.frontendproject.fragment.CounselBoardListFragment

class CounselBoardActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityCounselBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.counselBoardToolbar)
        supportActionBar?.title = "상담 게시판"

        supportFragmentManager.beginTransaction()
            .replace(R.id.counselBoardFragmentContainer, CounselBoardListFragment())
            .commit()
    }
}