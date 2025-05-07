package com.example.frontendproject

import android.content.Intent
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.frontendproject.databinding.ActivityQuizBinding
import com.example.frontendproject.model.QuizDto

class QuizActivity : AppCompatActivity() {

    private lateinit var quizList: List<QuizDto>
    private var currentIndex = 0
    private var score = 0
    private lateinit var binding: ActivityQuizBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        quizList = intent.getParcelableArrayListExtra("quizList")!!
        score = 0
        currentIndex = 0

        showQuestion()

        binding.quizNextButton.setOnClickListener {
            val selectedId = binding.quizOptionGroup.checkedRadioButtonId
            if (selectedId != -1) {
                val selectedText = findViewById<RadioButton>(selectedId).text.toString()
                if (selectedText == quizList[currentIndex].answer) {
                    score += 10
                }

                currentIndex++
                if (currentIndex < quizList.size) {
                    showQuestion()
                } else {
                    val intent = Intent(this, QuizResultActivity::class.java)
                    intent.putExtra("score", score)
                    intent.putExtra("total", quizList.size)
                    startActivity(intent)
                    finish()
                }
            } else {
                Toast.makeText(this, "옵션을 선택해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showQuestion() {
        val question = quizList[currentIndex]
        binding.quizQuestionText.text = question.question

        binding.quizOptionGroup.removeAllViews()
        question.options.forEach { option ->
            val radioButton = RadioButton(this)
            radioButton.text = option
            binding.quizOptionGroup.addView(radioButton)
        }
    }
}