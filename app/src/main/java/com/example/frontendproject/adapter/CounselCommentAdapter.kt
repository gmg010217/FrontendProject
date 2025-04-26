package com.example.frontendproject.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.frontendproject.MainActivity
import com.example.frontendproject.databinding.ItemCounselBoardCommentBinding
import com.example.frontendproject.model.counselboard.CommentResponse
import com.example.frontendproject.network.ApiService
import com.example.frontendproject.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CounselCommentViewHolder(val binding: ItemCounselBoardCommentBinding) : RecyclerView.ViewHolder(binding.root)

class CounselCommentAdapter(
    private var commentList: List<CommentResponse>,
    private val memberId: Long
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int = commentList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        CounselCommentViewHolder(ItemCounselBoardCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as CounselCommentViewHolder).binding
        val comment = commentList[position]

        binding.counselBoardCommentWriter.text = comment.writerName
        binding.counselBoardCommentContent.text = comment.content

        binding.counselBoardDeleteBtn.setOnClickListener {
            val context = holder.itemView.context
            val api = RetrofitClient.retrofit.create(ApiService::class.java)

            api.deleteCounselBoardCommnet(memberId, comment.id).enqueue(object: Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        Toast.makeText(context, "댓글 삭제 완료", Toast.LENGTH_SHORT).show()
                        val intent = Intent(context, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        context.startActivity(intent)
                    } else {
                        Toast.makeText(context, "작성자만 삭제할 수 있습니다", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(context, "서버 오류", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
    fun updateComments(newList: List<CommentResponse>) {
        commentList = newList
        notifyDataSetChanged()
    }
}