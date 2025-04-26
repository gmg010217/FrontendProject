package com.example.frontendproject.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.frontendproject.R
import com.example.frontendproject.adapter.CounselBoardAdapter
import com.example.frontendproject.model.counselboard.CounselboardsResponse
import com.example.frontendproject.network.ApiService
import com.example.frontendproject.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CounselBoardListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CounselBoardAdapter
    private var fullList: List<CounselboardsResponse> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_counsel_board_list, container, false)
        recyclerView = view.findViewById(R.id.counselBoardRecyclerView)

        val writeBtn = view.findViewById<Button>(R.id.counselBoardWriteBtn)
        writeBtn.setOnClickListener {
            val writeFragment = CounselBoardDetailFragment().apply {
                arguments = Bundle().apply {
                    putLong("counselBoardId", -1L)
                }
            }

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.counselBoardFragmentContainer, writeFragment)
                .addToBackStack(null)
                .commit()
        }

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.addItemDecoration(
            DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        )

        loadData()

        return view
    }

    private fun loadData() {
        val api = RetrofitClient.retrofit.create(ApiService::class.java)
        api.getCounselBoardList().enqueue(object : Callback<List<CounselboardsResponse>> {
            override fun onResponse(
                call: Call<List<CounselboardsResponse>>,
                response: Response<List<CounselboardsResponse>>
            ) {
                if (response.isSuccessful) {
                    fullList = response.body() ?: emptyList()
                    adapter = CounselBoardAdapter(fullList) { selectedCounselBoard ->
                        val detailFragment = CounselBoardDetailFragment().apply {
                            arguments = Bundle().apply {
                                putLong("counselBoardId", selectedCounselBoard.id)
                            }
                        }

                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.counselBoardFragmentContainer, detailFragment)
                            .addToBackStack(null)
                            .commit()
                    }
                    recyclerView.adapter = adapter
                }
            }

            override fun onFailure(call: Call<List<CounselboardsResponse>>, t: Throwable) {
                Toast.makeText(requireContext(), "서버 연결 실패", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_counsel_board, menu)

        val searchItem = menu.findItem(R.id.counselBoardSearch)
        val searchView = searchItem.actionView as SearchView

        searchView.queryHint = "제목으로 검색"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val keyword = query.orEmpty().trim()
                if (keyword.isNotEmpty()) {
                    searchFromServer(keyword)
                } else {
                    loadData() // 검색어 없으면 전체 목록 로드
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun searchFromServer(title: String) {
        val api = RetrofitClient.retrofit.create(ApiService::class.java)
        api.searchCounselBoardList(title).enqueue(object : Callback<List<CounselboardsResponse>> {
            override fun onResponse(
                call: Call<List<CounselboardsResponse>>,
                response: Response<List<CounselboardsResponse>>
            ) {
                if (response.isSuccessful) {
                    val result = response.body() ?: emptyList()
                    adapter = CounselBoardAdapter(result) { selectedCounselBoard ->
                        val detailFragment = CounselBoardDetailFragment().apply {
                            arguments = Bundle().apply {
                                putLong("counselBoardId", selectedCounselBoard.id)
                            }
                        }

                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.counselBoardFragmentContainer, detailFragment)
                            .addToBackStack(null)
                            .commit()
                    }
                    recyclerView.adapter = adapter
                }
            }

            override fun onFailure(call: Call<List<CounselboardsResponse>>, t: Throwable) {
                Toast.makeText(requireContext(), "서버 오류", Toast.LENGTH_SHORT).show()
            }
        })
    }
}