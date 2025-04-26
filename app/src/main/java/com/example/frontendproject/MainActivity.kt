package com.example.frontendproject

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
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
    private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.mainToolbar)

        drawerToggle = ActionBarDrawerToggle(
            this,
            binding.mainDrawerLayout,
            binding.mainToolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.mainDrawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        api = RetrofitClient.retrofit.create(ApiService::class.java)

        val memberId = getSharedPreferences("user_prefs", MODE_PRIVATE).getLong("memberId", -1)
        if (memberId != null) {
            fetchUserInfo(memberId)
        }

        binding.exerciseCalendarBtn.setOnClickListener {
            startActivity(Intent(this, ExerciseCalendarActivity::class.java))
        }
        binding.aiChatBtn.setOnClickListener {
            startActivity(Intent(this, AiChatActivity::class.java))
        }
        binding.freeBoardBtn.setOnClickListener {
            startActivity(Intent(this, FreeBoardActivity::class.java))
        }
        binding.worryBoardBtn.setOnClickListener {
            startActivity(Intent(this, CounselBoardActivity::class.java))
        }
        binding.rankingBoardBtn.setOnClickListener {
            startActivity(Intent(this, RankingBoardActivity::class.java))
        }

        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navLogout -> {
                    logout()
                    true
                }
                R.id.navEditMember -> {
                    val intent = Intent(this@MainActivity, EditMemberActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navDiary -> {
                    val intent = Intent(this@MainActivity, DiaryActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navHome -> {
                    binding.mainDrawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.navDeleteMember -> {
                    api.deleteMember(memberId).enqueue(object: Callback<String> {
                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            if (response.isSuccessful) {
                                val intent = Intent(this@MainActivity, IntroActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                            } else {
                                Toast.makeText(this@MainActivity, "회원 탈퇴에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onFailure(call: Call<String>, t: Throwable) {
                            Toast.makeText(this@MainActivity, "서버 연결 실패", Toast.LENGTH_SHORT).show()
                        }
                    })
                    true
                }
                else -> {
                    true
                }
            }
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

                    nicknameTextView.text = userInfo?.nickName ?: "알 수 없음"
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

    override fun onBackPressed() {
        if (binding.mainDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.mainDrawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun logout() {
        api.logout().enqueue(object: Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    getSharedPreferences("user_prefs", MODE_PRIVATE)
                        .edit()
                        .clear()
                        .apply()

                    val intent = Intent(this@MainActivity, IntroActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    Toast.makeText(this@MainActivity, "로그아웃 실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(this@MainActivity, "서버 연결 실패", Toast.LENGTH_SHORT).show()
            }
        })
    }
}