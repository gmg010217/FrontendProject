package com.example.frontendproject.fragment

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.frontendproject.MainActivity
import com.example.frontendproject.R
import com.example.frontendproject.adapter.CommentAdapter
import com.example.frontendproject.model.FreeBoardAddRequest
import com.example.frontendproject.model.FreeBoardResponse
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
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        val memberId = requireContext().getSharedPreferences("user_prefs", MODE_PRIVATE).getLong("memberId", -1)
        val freeBoardId = arguments?.getLong("freeBoardId") ?: -1L

        lateinit var view: View

        if (freeBoardId == -1L) {
            view = inflater.inflate(R.layout.fragment_free_board_add, container, false)
            freeBoardAdd(view, memberId)
        } else {
            view = inflater.inflate(R.layout.fragment_free_board_detail, container, false)
            freeBoardDetail(view, memberId, freeBoardId)
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

    private fun freeBoardDetail(view: View, memberId: Long, freeBoardId: Long) {
        val titleView = view.findViewById<EditText>(R.id.freeBoardDetailTitle)
        val contentView = view.findViewById<EditText>(R.id.freeBoardDetailContent)
        val commentInput = view.findViewById<EditText>(R.id.freeBoardCommentContent)
        val saveBtn = view.findViewById<Button>(R.id.freeBoardDetailSaveBtn)
        val commentRecyclerView = view.findViewById<RecyclerView>(R.id.freeBoardCommentRecyclerView)
        val writerNameTextView = view.findViewById<TextView>(R.id.freeBoardDetailWriter)

        val commentAdapter = CommentAdapter(emptyList()) { }
        commentRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        commentRecyclerView.adapter = commentAdapter

        api.getFreeboard(memberId, freeBoardId).enqueue(object: Callback<FreeBoardResponse> {
            override fun onResponse(
                call: Call<FreeBoardResponse>,
                response: Response<FreeBoardResponse>
            ) {
                if (response.isSuccessful) {
                    val detail = response.body()
                    titleView.setText(detail?.title ?: "")
                    contentView.setText(detail?.content ?: "")
                    writerNameTextView.text = "작성자명 : ${detail?.writerName ?: ""}"
                    commentAdapter.updateComments(detail?.comments ?: emptyList())
                }
            }

            override fun onFailure(call: Call<FreeBoardResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "서버 오류", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun moveToMain() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
    }
}