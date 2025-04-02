package com.example.frontendproject.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.frontendproject.LoginActivity
import com.example.frontendproject.R
import com.example.frontendproject.model.SignUpRequest
import com.example.frontendproject.network.ApiService
import com.example.frontendproject.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupStep2Fragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_signup_step2, container, false)

        val nicknameEditText = view.findViewById<EditText>(R.id.nicknameEditText)
        val ageEditText = view.findViewById<EditText>(R.id.ageEditText)
        val genderRadioGroup = view.findViewById<RadioGroup>(R.id.genderRadioGroup)
        val aboutMeEditText = view.findViewById<EditText>(R.id.aboutMeEditText)

        val prevButton = view.findViewById<Button>(R.id.joinPrevBtn)
        prevButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        val finishBtn = view.findViewById<Button>(R.id.joinFinishBtn)
        finishBtn.setOnClickListener {
            val emailId = arguments?.getString("emailId") ?: ""
            val password = arguments?.getString("password") ?: ""

            val nickName = nicknameEditText.text.toString() ?: ""
            val age = ageEditText.text.toString().toIntOrNull() ?: 0

            val selectedGenderId = genderRadioGroup.checkedRadioButtonId
            val genderRadioButton = view.findViewById<RadioButton>(selectedGenderId)
            val gender = genderRadioButton?.text.toString() ?: ""

            val aboutMe = aboutMeEditText.text.toString()

            val request = SignUpRequest(
                emailId = emailId,
                password = password,
                nickName = nickName,
                age = age,
                gender = gender,
                aboutMe = aboutMe
            )

            val api = RetrofitClient.retrofit.create(ApiService::class.java)
            api.signUp(request).enqueue(object: Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        Toast.makeText(requireContext(), "회원가입 성공!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(requireContext(), LoginActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(requireContext(), "회원가입 실패, 입력한 정보를 다시 확인해주세요", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Toast.makeText(requireContext(), "에러 발생! 다시 시도해주세요", Toast.LENGTH_SHORT).show()
                    Log.d("gwak", "${t.message}")
                }
            })

        }

        return view
    }
}