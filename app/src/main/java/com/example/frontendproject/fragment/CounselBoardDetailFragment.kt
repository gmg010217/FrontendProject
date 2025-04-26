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
import com.example.frontendproject.adapter.CounselCommentAdapter
import com.example.frontendproject.model.counselboard.CounselBoardAddRequest
import com.example.frontendproject.model.counselboard.CounselBoardCommentAddRequest
import com.example.frontendproject.model.counselboard.CounselBoardResponse
import com.example.frontendproject.network.ApiService
import com.example.frontendproject.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CounselBoardDetailFragment : Fragment() {
    val api = RetrofitClient.retrofit.create(ApiService::class.java)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        val memberId = requireContext().getSharedPreferences("user_prefs", MODE_PRIVATE).getLong("memberId", -1)
        val counselBoardId = arguments?.getLong("counselBoardId") ?: -1L
        val editMode = arguments?.getBoolean("isEditMode") ?: false

        lateinit var view: View

        if (counselBoardId == -1L || editMode) {
            view = inflater.inflate(R.layout.fragment_counsel_board_add, container, false)
            counselBoardAdd(view, memberId, counselBoardId)
        } else {
            view = inflater.inflate(R.layout.fragment_counsel_board_detail, container, false)
            counselBoardDetail(view, memberId, counselBoardId)
        }

        return view
    }

    private fun counselBoardAdd(view: View, memberId: Long, counselBoardId: Long) {
        val saveBtn = view.findViewById<Button>(R.id.counselBoardAddSaveBtn)

        if (counselBoardId != -1L) {
            api.getEditCounselBoard(memberId, counselBoardId).enqueue(object: Callback<CounselBoardAddRequest> {
                override fun onResponse(
                    call: Call<CounselBoardAddRequest>,
                    response: Response<CounselBoardAddRequest>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "수정을 위한 게시글 조회 완료", Toast.LENGTH_SHORT).show()

                        val data = response.body()
                        val titleEditText = view.findViewById<EditText>(R.id.counselBoardAddTitle)
                        val contentEditText = view.findViewById<EditText>(R.id.counselBoardAddContent)

                        titleEditText.setText(data?.title ?: "")
                        contentEditText.setText(data?.content ?: "")

                        titleEditText.isEnabled = true
                        contentEditText.isEnabled = true
                    } else {
                        Toast.makeText(requireContext(), "작성자만 수정할 수 있습니다.", Toast.LENGTH_SHORT).show()
                        moveToMain()
                    }
                }

                override fun onFailure(call: Call<CounselBoardAddRequest>, t: Throwable) {
                    Toast.makeText(requireContext(), "서버 오류", Toast.LENGTH_SHORT).show()
                }
            })
        }

        saveBtn.setOnClickListener {
            if (counselBoardId == -1L) {
                val title = view.findViewById<EditText>(R.id.counselBoardAddTitle).text.toString()
                val content = view.findViewById<EditText>(R.id.counselBoardAddContent).text.toString()
                val addCounselBoard = CounselBoardAddRequest(
                    title = title,
                    content = content
                )

                api.addCounselBoard(memberId, addCounselBoard).enqueue(object: Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if (response.isSuccessful) {
                            Toast.makeText(requireContext(), "게시글 작성 완료", Toast.LENGTH_SHORT).show()
                            moveToMain()
                        } else {
                            Toast.makeText(requireContext(), "저장 실패", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Toast.makeText(requireContext(), "서버 오류", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                val title = view.findViewById<EditText>(R.id.counselBoardAddTitle).text.toString()
                val content = view.findViewById<EditText>(R.id.counselBoardAddContent).text.toString()
                val data = CounselBoardAddRequest(
                    title = title,
                    content = content
                )

                api.editCounselBoard(memberId, counselBoardId, data).enqueue(object: Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if (response.isSuccessful) {
                            Toast.makeText(requireContext(), "수정이 완료 되었습니다", Toast.LENGTH_SHORT).show()
                            moveToMain()
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Toast.makeText(requireContext(), "서버 오류", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }

    private fun counselBoardDetail(view: View, memberId: Long, counselBoardId: Long) {
        val titleView = view.findViewById<EditText>(R.id.counselBoardDetailTitle)
        val contentView = view.findViewById<EditText>(R.id.counselBoardDetailContent)
        val commentRecyclerView = view.findViewById<RecyclerView>(R.id.counselBoardCommentRecyclerView)
        val writerNameTextView = view.findViewById<TextView>(R.id.counselBoardDetailWriter)

        val commentAdapter = CounselCommentAdapter(emptyList(), memberId)
        commentRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        commentRecyclerView.adapter = commentAdapter

        api.getCounselboard(memberId, counselBoardId).enqueue(object: Callback<CounselBoardResponse> {
            override fun onResponse(
                call: Call<CounselBoardResponse>,
                response: Response<CounselBoardResponse>
            ) {
                if (response.isSuccessful) {
                    val detail = response.body()
                    titleView.setText(detail?.title ?: "")
                    contentView.setText(detail?.content ?: "")
                    writerNameTextView.text = "작성자명 : ${detail?.writerName ?: ""}"
                    commentAdapter.updateComments(detail?.comments ?: emptyList())
                }
            }

            override fun onFailure(call: Call<CounselBoardResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "서버 오류", Toast.LENGTH_SHORT).show()
            }
        })

        val toolbar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.counselBoardDetailToolbar)
        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.counselBoardEdit -> {
                    moveToEdit(memberId, counselBoardId)
                    true
                }
                R.id.counselBoardDelete -> {
                    counselBoardDelete(memberId, counselBoardId)
                    true
                }
                else -> false
            }
        }

        val registerBtn = view.findViewById<Button>(R.id.counselBoardDetailSaveBtn)

        registerBtn.setOnClickListener {
            val commentContent = view.findViewById<EditText>(R.id.counselBoardDetailCommentContent).text.toString()

            val data = CounselBoardCommentAddRequest(comment = commentContent)
            api.addCounselBoardComment(memberId, counselBoardId, data).enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "댓글 작성에 성공하였습니다", Toast.LENGTH_SHORT).show()
                        moveToMain()
                    } else {
                        Toast.makeText(requireContext(), "댓글 작성에 실패하였습니다", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(requireContext(), "서버 오류", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun counselBoardDelete(memberId: Long, counselBoardId: Long) {
        api.deleteCounselBoard(memberId, counselBoardId).enqueue(object: Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "삭제가 완료되었습니다", Toast.LENGTH_SHORT).show()
                    moveToMain()
                } else {
                    Toast.makeText(requireContext(), "작성자만 삭제할 수 있습니다", Toast.LENGTH_SHORT).show()
                    moveToMain()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(requireContext(), "서버 오류", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun moveToEdit(memberId: Long, counselBoardId: Long) {
        val bundle = Bundle().apply {
            putLong("counselBoardId", counselBoardId)
            putBoolean("isEditMode", true)
        }

        val editFragment = CounselBoardDetailFragment()
        editFragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.counselBoardFragmentContainer, editFragment)
            .addToBackStack(null)
            .commit()
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