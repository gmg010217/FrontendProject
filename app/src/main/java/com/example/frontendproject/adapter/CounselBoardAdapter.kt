package com.example.frontendproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.frontendproject.databinding.ItemCounselBoardBinding
import com.example.frontendproject.model.counselboard.CounselboardsResponse


class CounselBoardViewHolder(val binding: ItemCounselBoardBinding) : RecyclerView.ViewHolder(binding.root)

class CounselBoardAdapter(
    private val originalList: List<CounselboardsResponse>,
    private val onItemClick: (CounselboardsResponse) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var filteredList = originalList.toMutableList()

    override fun getItemCount(): Int = filteredList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        CounselBoardViewHolder(
            ItemCounselBoardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as CounselBoardViewHolder).binding
        val counselBoard = filteredList[position]

        binding.counselBoardTitleText.text = counselBoard.title ?: "제목 없음"
        binding.counselBoardDateText.text = counselBoard.creatDate?.toString() ?: "날짜 없음"

        binding.root.setOnClickListener {
            onItemClick(counselBoard)
        }
    }

    fun filter(query: String) {
        filteredList = if (query.isEmpty()) {
            originalList.toMutableList()
        } else {
            originalList.filter {
                it.title.contains(query, ignoreCase = true)
            }.toMutableList()
        }
        notifyDataSetChanged()
    }
}