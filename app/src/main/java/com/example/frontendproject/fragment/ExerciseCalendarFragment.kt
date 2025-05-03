package com.example.frontendproject.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.fragment.app.Fragment
import com.example.frontendproject.R
import com.example.frontendproject.model.Quote
import com.example.frontendproject.network.ApiService
import com.example.frontendproject.network.RetrofitClient
import kotlinx.coroutines.selects.select
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import kotlin.text.Typography.quote

class ExerciseCalendarFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_exercise_calendar, container, false)

        val api = RetrofitClient.retrofit.create(ApiService::class.java)

        val exerciseQuoteTextView = view.findViewById<TextView>(R.id.exerciseQuoteTextView)
        val exerciseRecommand = view.findViewById<TextView>(R.id.exerciseRecommand)
        val calendarView = view.findViewById<CalendarView>(R.id.exerciseCalendarView)
        val moveBtn = view.findViewById<Button>(R.id.moveExerciseDetailBtn)

        var selectedDate: LocalDate = LocalDate.now()
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
        }

        val memberId = requireContext().getSharedPreferences("user_prefs", MODE_PRIVATE).getLong("memberId", -1)

        val todayDay = LocalDate.now().dayOfMonth
        api.getQuoteAndRecommand(memberId, todayDay.toLong()).enqueue(object : Callback<Quote> {
            override fun onResponse(call: Call<Quote>, response: Response<Quote>) {
                if (response.isSuccessful) {
                    val quote = response.body()
                    exerciseQuoteTextView.text = "오늘의 명언 : ${quote?.content ?: "명언 없음"}"
                    exerciseRecommand.text = "운동 루틴 추천 : ${quote?.recommand ?: "루틴 추천 없음"}"
                } else {
                    Toast.makeText(requireContext(), "명언을 불러오는데 실패하였습니다", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Quote>, t: Throwable) {
                Toast.makeText(requireContext(), "서버 연결 실패", Toast.LENGTH_SHORT).show()
            }
        })

        moveBtn.setOnClickListener {
            val bundle = Bundle().apply {
                putString("selectedDate", selectedDate.toString())
            }

            val fragment = ExerciseDetailFragment().apply {
                arguments = bundle
            }

            parentFragmentManager.beginTransaction()
                .replace(R.id.exerciseFragmentContainer, fragment)
                .addToBackStack(null)
                .commit()
        }

        return view
    }
}