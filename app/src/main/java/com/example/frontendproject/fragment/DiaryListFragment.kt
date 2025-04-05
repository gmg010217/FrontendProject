package com.example.frontendproject.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.frontendproject.R
import com.example.frontendproject.adapter.DiaryAdapter
import com.example.frontendproject.model.Diary
import com.example.frontendproject.network.ApiService
import com.example.frontendproject.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiaryListFragment: Fragment() {
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_diary_list, container, false)
        recyclerView = view.findViewById(R.id.diaryRecyclerView)

        val writeBtn = view.findViewById<Button>(R.id.diaryWriteBtn)
        writeBtn.setOnClickListener {
            val writeFragment = DiaryDetailFragment().apply {
                arguments = Bundle().apply {
                    putLong("diaryId", -1L)
                }
            }

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.diaryFragmentContainer, writeFragment)
                .addToBackStack(null)
                .commit()
        }

        val api = RetrofitClient.retrofit.create(ApiService::class.java)
        val memberId = requireContext().getSharedPreferences("user_prefs", MODE_PRIVATE).getLong("memberId", -1)
        api.getDiaryList(memberId).enqueue(object : Callback<List<Diary>> {
            override fun onResponse(call: Call<List<Diary>>, response: Response<List<Diary>>) {
                if (response.isSuccessful) {
                    val diaryList = response.body() ?: emptyList()
                    recyclerView.adapter = DiaryAdapter(diaryList) { selectedDiary ->
                        val detailFragment = DiaryDetailFragment().apply {
                            arguments = Bundle().apply {
                                putLong("diaryId", selectedDiary.id)
                            }
                        }

                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.diaryFragmentContainer, detailFragment)
                            .addToBackStack(null)
                            .commit()
                    }
                }
            }

            override fun onFailure(call: Call<List<Diary>>, t: Throwable) {
                Toast.makeText(requireContext(), "서버 연결 실패", Toast.LENGTH_SHORT).show()
            }
        })

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))

        return view
    }
}