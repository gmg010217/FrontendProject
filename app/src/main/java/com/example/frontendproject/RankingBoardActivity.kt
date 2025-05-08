package com.example.frontendproject

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.frontendproject.adapter.RankingAdapter
import com.example.frontendproject.databinding.ActivityRankBinding
import com.example.frontendproject.model.RankingItem
import com.example.frontendproject.network.ApiService
import com.example.frontendproject.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RankingBoardActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRankBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val api = RetrofitClient.retrofit.create(ApiService::class.java)
        val memberId = getSharedPreferences("user_prefs", MODE_PRIVATE).getLong("memberId", -1)

        api.getRank(memberId).enqueue(object: Callback<List<RankingItem>> {
            override fun onResponse(
                call: Call<List<RankingItem>>,
                response: Response<List<RankingItem>>
            ) {
                if (response.isSuccessful) {
                    var rankingList = response.body() ?: emptyList()

                    val myInfo = rankingList.firstOrNull()
                    val otherList = rankingList.drop(1)

                    binding.rankNickName.text = myInfo?.nickName ?: "나의 닉네임"
                    binding.rankScore.text = "총 점수: ${myInfo?.score ?: 0}점"
                    binding.ranking.text = "${myInfo?.ranking} 위"

                    val adapter = RankingAdapter(otherList)
                    binding.recyclerRanking.layoutManager = LinearLayoutManager(this@RankingBoardActivity)
                    binding.recyclerRanking.adapter = adapter
                    Toast.makeText(this@RankingBoardActivity, "랭킹 정보를 성공적으로 불러왔습니다", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@RankingBoardActivity, "랭킹 정보를 불러오는데 실패하였습니다", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<RankingItem>>, t: Throwable) {
                Toast.makeText(this@RankingBoardActivity, "서버 오류", Toast.LENGTH_SHORT).show()
            }
        })
    }
}