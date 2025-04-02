package com.example.frontendproject.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.frontendproject.R

class SignupStep2Fragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_signup_step2, container, false)

        val prevButton = view.findViewById<Button>(R.id.joinPrevBtn)
        prevButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        return view
    }
}