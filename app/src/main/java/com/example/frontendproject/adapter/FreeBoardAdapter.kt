package com.example.frontendproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.frontendproject.databinding.ItemFreeBoardBinding
import com.example.frontendproject.model.FreeboardsResponse

class FreeBoardViewHolder(val binding: ItemFreeBoardBinding): RecyclerView.ViewHolder(binding.root)

class FreeBoardAdapter (
    val freeBoardList: List<FreeboardsResponse>,
    val onItemClick: (FreeboardsResponse) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int = freeBoardList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        FreeBoardViewHolder(ItemFreeBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as FreeBoardViewHolder).binding
        val freeBoards = freeBoardList[position]

        binding.freeBoardTitleText.text = freeBoardList[position].title
        binding.freeBoardDateText.text = freeBoardList[position].creatDate.toString()

        binding.root.setOnClickListener {
            onItemClick(freeBoards)
        }
    }
}