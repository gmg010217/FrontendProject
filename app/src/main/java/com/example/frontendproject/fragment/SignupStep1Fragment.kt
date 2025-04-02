package com.example.frontendproject.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.frontendproject.R

class SignupStep1Fragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_signup_step1, container, false)

        val nextButton = view.findViewById<Button>(R.id.joinNextBtn)
        val passwordEditText = view.findViewById<EditText>(R.id.passwordEditText)
        val confirmEditText = view.findViewById<EditText>(R.id.confirmPasswordEditText)

        val emailIdEditText = view.findViewById<EditText>(R.id.eamilIdEditText)

        val email = emailIdEditText.text.toString()
        val password = passwordEditText.text.toString()

        val signupStep2Fragment = SignupStep2Fragment().apply {
            arguments = Bundle().apply {
                putString("emailId", email)
                putString("password", password)
            }
        }

        nextButton.setOnClickListener {
            val password = passwordEditText.text.toString()
            val confirm = confirmEditText.text.toString()

            if (password == confirm && password.isNotBlank()) {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.joinFragmentContainer, SignupStep2Fragment())
                    .addToBackStack(null)
                    .commit()
            } else {
                Toast.makeText(requireContext(), "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}