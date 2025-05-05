package com.example.frontendproject

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.app.ActivityCompat
import com.example.frontendproject.databinding.ActivityMainBinding
import com.example.frontendproject.model.MemberInfoResponse
import com.example.frontendproject.network.ApiService
import com.example.frontendproject.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var api: ApiService
    private lateinit var drawerToggle: ActionBarDrawerToggle

    // 걷음수 센서
    private lateinit var sensorManager: SensorManager
    private var stepSensor: Sensor? = null
    private var initialStepCount = 0f
    private val prefs by lazy {
        getSharedPreferences("step_prefs", Context.MODE_PRIVATE)
    }

    companion object {
        private const val PERMISSION_ACTIVITY_RECOGNITION = 1001
    }

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
        val memberId = getSharedPreferences("user_prefs", MODE_PRIVATE).getLong("memberId", -1L)
        if (memberId != -1L) {
            fetchUserInfo(memberId)
        }

        // 센서 초기화
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        // 이전 기준값 불러오기
        initialStepCount = prefs.getFloat("initialStepCount", 0f)

        requestActivityRecognitionPermission()

        // 버튼 클릭
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
                    startActivity(Intent(this, EditMemberActivity::class.java))
                    true
                }
                R.id.navDiary -> {
                    startActivity(Intent(this, DiaryActivity::class.java))
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
                                startActivity(Intent(this@MainActivity, IntroActivity::class.java).apply {
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                })
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
                else -> true
            }
        }
    }

    // 권한 요청
    private fun requestActivityRecognitionPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                PERMISSION_ACTIVITY_RECOGNITION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ACTIVITY_RECOGNITION &&
            grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
            // 권한 승인됨
        } else {
            Toast.makeText(this, "걸음 센서 권한이 필요합니다", Toast.LENGTH_SHORT).show()
        }
    }

    // SensorEventListener
    private val stepListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            val totalSteps = event.values[0]
            if (initialStepCount == 0f) {
                initialStepCount = totalSteps
                prefs.edit().putFloat("initialStepCount", initialStepCount).apply()
            }
            val todaySteps = (totalSteps - initialStepCount).toInt()
            binding.mainStep.text = "오늘 걸음 수: $todaySteps 걸음"
        }
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    override fun onResume() {
        super.onResume()
        resetDailyBaselineIfNeeded()
        stepSensor?.let {
            sensorManager.registerListener(stepListener, it, SensorManager.SENSOR_DELAY_NORMAL)
        } ?: Toast.makeText(this, "걸음 센서를 지원하지 않는 기기입니다", Toast.LENGTH_LONG).show()
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(stepListener)
    }

    // 매일 자정 기준값 리셋
    private fun resetDailyBaselineIfNeeded() {
        val lastDay = prefs.getLong("lastResetDay", 0L)
        val today = LocalDate.now().toEpochDay()
        if (lastDay != today) {
            prefs.edit().apply {
                putFloat("initialStepCount", 0f)
                putLong("lastResetDay", today)
                apply()
            }
            initialStepCount = 0f
        }
    }

    private fun fetchUserInfo(memberId: Long) {
        api.memberInfo(memberId).enqueue(object: Callback<MemberInfoResponse> {
            override fun onResponse(
                call: Call<MemberInfoResponse>,
                response: Response<MemberInfoResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { userInfo ->
                        val header = binding.navView.getHeaderView(0)
                        header.findViewById<TextView>(R.id.navNickname).text = userInfo.nickName
                        header.findViewById<TextView>(R.id.navEmailId).text = userInfo.emailId
                    }
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
                        .edit().clear().apply()
                    startActivity(Intent(this@MainActivity, IntroActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    })
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
