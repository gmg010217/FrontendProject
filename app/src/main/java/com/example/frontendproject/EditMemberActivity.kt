package com.example.frontendproject

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.frontendproject.databinding.ActivityEditMemberBinding
import com.example.frontendproject.model.MemberUpdate
import com.example.frontendproject.network.ApiService
import com.example.frontendproject.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditMemberActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditMemberBinding
    private lateinit var api: ApiService
    private var memberId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditMemberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        api = RetrofitClient.retrofit.create(ApiService::class.java)
        memberId = getSharedPreferences("user_prefs", MODE_PRIVATE).getLong("memberId", -1)

        if (memberId != -1L) {
            fetchMemberInfo(memberId)
        }

        binding.editJoinFinishBtn.setOnClickListener {
            val nickname = binding.editNicknameEditText.text.toString()
            val gender = when (binding.editGenderRadioGroup.checkedRadioButtonId) {
                R.id.editMaleRadioBtn -> "남성"
                R.id.editFemaleRadioBtn -> "여성"
                else -> ""
            }
            val age = binding.editAgeEditText.text.toString().toIntOrNull() ?: 0
            val aboutMe = binding.editAboutMeEditText.text.toString()

            val memberUpdate = MemberUpdate(nickname, age, gender ,aboutMe)
            updateMember(memberId, memberUpdate)
        }
    }

    private fun updateMember(memberId: Long, request: MemberUpdate) {
        api.updateMember(memberId ,request).enqueue(object: Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@EditMemberActivity, "수정 성공", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@EditMemberActivity, "수정 실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(this@EditMemberActivity, "서버 연결 실패", Toast.LENGTH_SHORT)
            }
        })
    }

    private fun fetchMemberInfo(memberId: Long) {
        api.getMemberInfo(memberId).enqueue(object: Callback<MemberUpdate> {
            override fun onResponse(call: Call<MemberUpdate>, response: Response<MemberUpdate>) {
                if (response.isSuccessful) {
                    val info = response.body()
                    info?.let {
                        binding.editNicknameEditText.setText(it.nickName)
                        binding.editAgeEditText.setText(it.age.toString())
                        binding.editAboutMeEditText.setText(it.aboutMe)

                        when (it.gender) {
                            "남성" -> binding.editGenderRadioGroup.check(R.id.editMaleRadioBtn)
                            "여성" -> binding.editGenderRadioGroup.check(R.id.editFemaleRadioBtn)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<MemberUpdate>, t: Throwable) {
                Toast.makeText(this@EditMemberActivity, "회원 정보 불러오기 실패", Toast.LENGTH_SHORT).show()
            }
        })
    }
}