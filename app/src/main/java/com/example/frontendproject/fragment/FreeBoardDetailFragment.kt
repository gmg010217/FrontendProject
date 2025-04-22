package com.example.frontendproject.fragment

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.frontendproject.MainActivity
import com.example.frontendproject.R
import com.example.frontendproject.model.FreeBoardAddRequest
import com.example.frontendproject.network.ApiService
import com.example.frontendproject.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FreeBoardDetailFragment : Fragment() {
    val api = RetrofitClient.retrofit.create(ApiService::class.java)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val memberId = requireContext().getSharedPreferences("user_prefs", MODE_PRIVATE).getLong("memberId", -1)
        val freeBoardId = arguments?.getLong("freeBoardId") ?: -1L

        lateinit var view: View

        if (freeBoardId == -1L) {
            view = inflater.inflate(R.layout.fragment_free_board_add, container, false)
            freeBoardAdd(view, memberId)
        } else {
            view = inflater.inflate(R.layout.fragment_free_board_detail, container, false)
            freeBoardDetail(view, memberId)
        }

        return view
    }

    private fun freeBoardAdd(view: View, memberId: Long) {
        val saveBtn = view.findViewById<Button>(R.id.freeBoardAddSaveBtn)

        saveBtn.setOnClickListener {
            val title = view.findViewById<EditText>(R.id.freeBoardAddTitle).text.toString()
            val content = view.findViewById<EditText>(R.id.freeBoardAddContent).text.toString()
            val addFreeBoard = FreeBoardAddRequest(
                title = title,
                content = content
            )

            api.addFreeBoard(memberId, addFreeBoard).enqueue(object: Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "게시글 작성 완료", Toast.LENGTH_SHORT).show()
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
    }

    private fun freeBoardDetail(view: View, memberId: Long) {

    }

    private fun moveToMain() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}