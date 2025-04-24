package com.example.frontendproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.frontendproject.databinding.ItemFreeBoardCommentBinding
import com.example.frontendproject.model.CommentResponse

class CommentViewHolder(val binding: ItemFreeBoardCommentBinding) : RecyclerView.ViewHolder(binding.root)

class CommentAdapter(
    private var commentList: List<CommentResponse>,
    private val onItemClick: (CommentResponse) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int = commentList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        CommentViewHolder(ItemFreeBoardCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as CommentViewHolder).binding
        val comment = commentList[position]

        binding.freeBoardCommentWriter.text = comment.writerName
        binding.freeBoardCommentContent.text = comment.content

        binding.root.setOnClickListener {
            onItemClick(comment)
        }
    }
    fun updateComments(newList: List<CommentResponse>) {
        commentList = newList
        notifyDataSetChanged()
    }
}