package com.example.frontendproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.frontendproject.databinding.ItemDiaryBinding
import com.example.frontendproject.model.Diary

class DiaryViewHolder(val binding: ItemDiaryBinding): RecyclerView.ViewHolder(binding.root)

class DiaryAdapter(val diaryList: List<Diary>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int = diaryList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        DiaryViewHolder(ItemDiaryBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as DiaryViewHolder).binding

        binding.diaryTitleText.text = diaryList[position].title
        binding.diarydateText.text = diaryList[position].diaryDate


    }
}