package com.example.frontendproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.frontendproject.databinding.ItemFreeBoardBinding
import com.example.frontendproject.model.freeboard.FreeboardsResponse


class FreeBoardViewHolder(val binding: ItemFreeBoardBinding) : RecyclerView.ViewHolder(binding.root)

class FreeBoardAdapter(
    private val originalList: List<FreeboardsResponse>,
    private val onItemClick: (FreeboardsResponse) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var filteredList = originalList.toMutableList()

    override fun getItemCount(): Int = filteredList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        FreeBoardViewHolder(
            ItemFreeBoardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as FreeBoardViewHolder).binding
        val freeBoard = filteredList[position]

        binding.freeBoardTitleText.text = freeBoard.title ?: "제목 없음"
        binding.freeBoardDateText.text = freeBoard.creatDate?.toString() ?: "날짜 없음"

        binding.root.setOnClickListener {
            onItemClick(freeBoard)
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