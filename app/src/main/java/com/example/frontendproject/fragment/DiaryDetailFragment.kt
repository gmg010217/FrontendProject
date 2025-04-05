package com.example.frontendproject.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.fragment.app.Fragment
import com.example.frontendproject.MainActivity
import com.example.frontendproject.R
import com.example.frontendproject.model.Diary
import com.example.frontendproject.network.ApiService
import com.example.frontendproject.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiaryDetailFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_diary_detail, container, false)
        val memberId = requireContext().getSharedPreferences("user_prefs", MODE_PRIVATE).getLong("memberId", -1)
        val diaryId = arguments?.getLong("diaryId") ?: -1L

        val titleEditText = view.findViewById<EditText>(R.id.diaryDetailTitle)
        val contentEditText = view.findViewById<EditText>(R.id.diaryDetailContent)
        val saveBtn = view.findViewById<Button>(R.id.diaryDetailSaveBtn)
        val deleteBtn = view.findViewById<Button>(R.id.diaryDetailDeleteBtn)

        val api = RetrofitClient.retrofit.create(ApiService::class.java)
        api.getDiaryDetail(memberId, diaryId).enqueue(object: Callback<Diary> {
            override fun onResponse(call: Call<Diary>, response: Response<Diary>) {
                if (response.isSuccessful) {
                    val diary = response.body()
                    view.findViewById<EditText>(R.id.diaryDetailTitle).setText(diary?.title ?: "")
                    view.findViewById<EditText>(R.id.diaryDetailContent).setText(diary?.content ?: "")
                }
            }

            override fun onFailure(call: Call<Diary>, t: Throwable) {
                Toast.makeText(requireContext(), "일기 불러오기 실패", Toast.LENGTH_SHORT).show()
            }
        })

        saveBtn.setOnClickListener {
            val updateDiary = Diary(
                id = diaryId,
                title = titleEditText.text.toString(),
                content = contentEditText.text.toString(),
                diaryDate = ""
            )

            api.updateDiary(memberId, diaryId, updateDiary).enqueue(object: Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "일기 저장 완료", Toast.LENGTH_SHORT).show()
                        moveToMain()
                    } else {
                        Toast.makeText(requireContext(), "저장 실패", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(requireContext(), "서버 오류", Toast.LENGTH_SHORT)
                }
            })
        }

        deleteBtn.setOnClickListener {
            api.deleteDiary(memberId, diaryId).enqueue(object: Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "일기 삭제 완료", Toast.LENGTH_SHORT)
                        moveToMain()
                    } else {
                        Toast.makeText(requireContext(), "삭제 실패", Toast.LENGTH_SHORT)
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(requireContext(), "서버 오류", Toast.LENGTH_SHORT)
                }
            })
        }

        return view
    }

    private fun moveToMain() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}