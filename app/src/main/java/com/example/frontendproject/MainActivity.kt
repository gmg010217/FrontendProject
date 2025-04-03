package com.example.frontendproject

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.frontendproject.databinding.ActivityMainBinding
import com.example.frontendproject.model.MemberInfoResponse
import com.example.frontendproject.network.ApiService
import com.example.frontendproject.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var api: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        api = RetrofitClient.retrofit.create(ApiService::class.java)

        val memberId = getSharedPreferences("user_prefs", MODE_PRIVATE).getLong("memberId", -1)
        if (memberId != null) {
            fetchUserInfo(memberId)
        }
    }

    private fun fetchUserInfo(memberId: Long) {
        api.memberInfo(memberId).enqueue(object: Callback<MemberInfoResponse> {
            override fun onResponse(
                call: Call<MemberInfoResponse>,
                response: Response<MemberInfoResponse>
            ) {
                if (response.isSuccessful) {
                    val userInfo = response.body()
                    val headerView = binding.navView.getHeaderView(0)

                    val nicknameTextView = headerView.findViewById<TextView>(R.id.navNickname)
                    val emailTextView = headerView.findViewById<TextView>(R.id.navEmailId)

                    nicknameTextView.text = userInfo?.nickname ?: "알 수 없음"
                    emailTextView.text = userInfo?.emailId ?: "이메일 없음"
                } else {
                    Toast.makeText(this@MainActivity, "회원 정보를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MemberInfoResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "서버 통신 오류가 발생했습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}