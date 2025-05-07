package com.example.frontendproject

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.frontendproject.databinding.ActivityQuizResultBinding
import com.example.frontendproject.model.Quiz
import com.example.frontendproject.network.ApiService
import com.example.frontendproject.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuizResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityQuizResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val api = RetrofitClient.retrofit.create(ApiService::class.java)
        val memberId = getSharedPreferences("user_prefs", MODE_PRIVATE).getLong("memberId", -1)

        val score = intent.getIntExtra("score", 0)
        val total = intent.getIntExtra("total", 10)

        binding.quizResultText.text = "당신의 점수: $score / ${total * 10}"

        val quiz = Quiz(memberId, score / 10)

        api.saveQuiz(memberId, quiz).enqueue(object: Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@QuizResultActivity, "결과 저장 완료", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@QuizResultActivity, "결과 저장 실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(this@QuizResultActivity, "서버 오류 발생", Toast.LENGTH_SHORT).show()
            }
        })

        binding.quizHomeButton.setOnClickListener {
            val intent = Intent(this@QuizResultActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            finish()
        }
    }
}