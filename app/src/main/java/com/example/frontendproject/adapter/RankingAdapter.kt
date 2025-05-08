package com.example.frontendproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.frontendproject.databinding.ItemRankingBinding
import com.example.frontendproject.model.RankingItem

class RankingViewHolder(val binding: ItemRankingBinding) : RecyclerView.ViewHolder(binding.root)

class RankingAdapter(
    val rankingList: List<RankingItem>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int = rankingList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        RankingViewHolder(ItemRankingBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as RankingViewHolder).binding
        val item = rankingList[position]

        binding.rankItemNickName.text = item.nickName
        binding.rankItemScore.text = "총 점수: ${item.score}점"
        binding.itemRanking.text = "${item.ranking} 위"
    }
}