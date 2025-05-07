package com.example.frontendproject

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.frontendproject.databinding.ActivityQuizStartBinding
import com.example.frontendproject.model.QuizDto
import com.example.frontendproject.network.ApiService
import com.example.frontendproject.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuizStartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityQuizStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val api = RetrofitClient.retrofit.create(ApiService::class.java)
        val memberId = getSharedPreferences("user_prefs", MODE_PRIVATE).getLong("memberId", -1)

        binding.quizStartBtn.setOnClickListener {
            Toast.makeText(this@QuizStartActivity, "퀴즈를 불러오고 있습니다", Toast.LENGTH_SHORT).show()
            api.getQuiz(memberId).enqueue(object: Callback<List<QuizDto>> {
                override fun onResponse(
                    call: Call<List<QuizDto>>,
                    response: Response<List<QuizDto>>
                ) {
                    if (response.isSuccessful) {
                        val quizList = response.body()
                        val intent = Intent(this@QuizStartActivity, QuizActivity::class.java)
                        intent.putParcelableArrayListExtra("quizList", ArrayList(quizList!!))
                        intent.putExtra("index", 0)
                        intent.putExtra("score", 0)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@QuizStartActivity, "퀴즈를 불러오는 중 오류 발생", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<QuizDto>>, t: Throwable) {
                    Toast.makeText(this@QuizStartActivity, "서버 오류 발생", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}