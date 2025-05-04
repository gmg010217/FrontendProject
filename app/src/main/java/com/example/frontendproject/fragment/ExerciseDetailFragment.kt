package com.example.frontendproject.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.fragment.app.Fragment
import com.example.frontendproject.MainActivity
import com.example.frontendproject.R
import com.example.frontendproject.model.Exercise
import com.example.frontendproject.network.ApiService
import com.example.frontendproject.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExerciseDetailFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_exercise_detail, container, false)

        val selectedDate = arguments?.getString("selectedDate").toString()

        val dayTextView = view.findViewById<TextView>(R.id.exerciseDetailDayTextView)
        dayTextView.text = selectedDate

        val titleEditText = view.findViewById<EditText>(R.id.exerciseDetailTitleEditView)
        val contentEditText = view.findViewById<EditText>(R.id.exerciseDetailContentEditView)
        val saveBtn = view.findViewById<Button>(R.id.exerciseDetailSaveButton)

        val api = RetrofitClient.retrofit.create(ApiService::class.java)
        val memberId = requireContext().getSharedPreferences("user_prefs", MODE_PRIVATE).getLong("memberId", -1)

        var count = arguments?.getLong("count")


        selectedDate?.let { date ->
            api.getExercise(memberId, selectedDate).enqueue(object: Callback<Exercise> {
                override fun onResponse(call: Call<Exercise>, response: Response<Exercise>) {
                    val exercise = response.body()
                    if (response.isSuccessful && count != 0L && !exercise?.title.isNullOrEmpty() && !exercise?.content.isNullOrEmpty()) {
                        exercise?.let {
                            titleEditText.setText(it.title)
                            contentEditText.setText(it.content)
                            setSaveButton(api, memberId, date, titleEditText, contentEditText, selectedDate, false)
                        }
                    } else if (response.isSuccessful && count == 0L) {
                        setSaveButton(api, memberId, date, titleEditText, contentEditText, selectedDate, true)
                    } else {
                        Toast.makeText(requireContext(), "AI가 운동 기록을 자동으로 작성중입니다.", Toast.LENGTH_SHORT).show()
                        api.getFirstExercise(memberId, selectedDate).enqueue(object: Callback<Exercise> {
                            override fun onResponse(call: Call<Exercise>, response: Response<Exercise>) {
                                if (response.isSuccessful) {
                                    val aiExercise = response.body()
                                    aiExercise?.let {
                                        titleEditText.setText(it.title)
                                        contentEditText.setText(it.content)
                                        setSaveButton(api, memberId, date, titleEditText, contentEditText, selectedDate, false)
                                    }
                                }
                            }

                            override fun onFailure(call: Call<Exercise>, t: Throwable) {
                                Toast.makeText(requireContext(), "서버 오류 발생", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                }

                override fun onFailure(call: Call<Exercise>, t: Throwable) {
                    Toast.makeText(requireContext(), "운동 데이터를 불러오지 못했습니다", Toast.LENGTH_SHORT).show()
                }
            })
        }

        return view
    }

    private fun setSaveButton(
        api: ApiService,
        memberId: Long,
        date: String,
        titleEditText: EditText?,
        contentEditText: EditText?,
        selectedDate: String?,
        realNew: Boolean
    ) {
        val saveBtn = view?.findViewById<Button>(R.id.exerciseDetailSaveButton)
        saveBtn?.setOnClickListener {
            if (realNew) {
                val addExercise = Exercise (
                    exerciseDate = selectedDate,
                    title = titleEditText?.text.toString(),
                    content = contentEditText?.text.toString()
                )

                api.addExercise(memberId, addExercise).enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if (response.isSuccessful) {
                            Toast.makeText(requireContext(), "운동 기록 저장 완료", Toast.LENGTH_SHORT).show()
                            moveToMain()
                        } else {
                            Toast.makeText(requireContext(), "운동 데이터 저장 실패", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Toast.makeText(requireContext(), "서버 오류 발생", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                val editExercise = Exercise (
                    exerciseDate = selectedDate,
                    title = titleEditText?.text.toString(),
                    content = contentEditText?.text.toString()
                )

                api.editExercises(memberId, selectedDate, editExercise).enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        Toast.makeText(requireContext(), "운동 기록 수정 완료", Toast.LENGTH_SHORT).show()
                        moveToMain()
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Toast.makeText(requireContext(), "서버 오류 발생", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }

    private fun moveToMain() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}