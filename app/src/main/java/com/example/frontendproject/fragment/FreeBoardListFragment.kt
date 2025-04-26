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
import com.example.frontendproject.adapter.FreeBoardAdapter
import com.example.frontendproject.model.freeboard.FreeboardsResponse
import com.example.frontendproject.network.ApiService
import com.example.frontendproject.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FreeBoardListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FreeBoardAdapter
    private var fullList: List<FreeboardsResponse> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_free_board_list, container, false)
        recyclerView = view.findViewById(R.id.freeBoardRecyclerView)

        val writeBtn = view.findViewById<Button>(R.id.freeBoardWriteBtn)
        writeBtn.setOnClickListener {
            val writeFragment = FreeBoardDetailFragment().apply {
                arguments = Bundle().apply {
                    putLong("freeBoardId", -1L)
                }
            }

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.freeBoardFragmentContainer, writeFragment)
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
        api.getFreeBoardList().enqueue(object : Callback<List<FreeboardsResponse>> {
            override fun onResponse(
                call: Call<List<FreeboardsResponse>>,
                response: Response<List<FreeboardsResponse>>
            ) {
                if (response.isSuccessful) {
                    fullList = response.body() ?: emptyList()
                    adapter = FreeBoardAdapter(fullList) { selectedFreeBoard ->
                        val detailFragment = FreeBoardDetailFragment().apply {
                            arguments = Bundle().apply {
                                putLong("freeBoardId", selectedFreeBoard.id)
                            }
                        }

                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.freeBoardFragmentContainer, detailFragment)
                            .addToBackStack(null)
                            .commit()
                    }
                    recyclerView.adapter = adapter
                }
            }

            override fun onFailure(call: Call<List<FreeboardsResponse>>, t: Throwable) {
                Toast.makeText(requireContext(), "서버 연결 실패", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_free_board, menu)

        val searchItem = menu.findItem(R.id.freeBoardSearch)
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
        api.searchFreeBoardList(title).enqueue(object : Callback<List<FreeboardsResponse>> {
            override fun onResponse(
                call: Call<List<FreeboardsResponse>>,
                response: Response<List<FreeboardsResponse>>
            ) {
                if (response.isSuccessful) {
                    val result = response.body() ?: emptyList()
                    adapter = FreeBoardAdapter(result) { selectedFreeBoard ->
                        val detailFragment = FreeBoardDetailFragment().apply {
                            arguments = Bundle().apply {
                                putLong("freeBoardId", selectedFreeBoard.id)
                            }
                        }

                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.freeBoardFragmentContainer, detailFragment)
                            .addToBackStack(null)
                            .commit()
                    }
                    recyclerView.adapter = adapter
                }
            }

            override fun onFailure(call: Call<List<FreeboardsResponse>>, t: Throwable) {
                Toast.makeText(requireContext(), "서버 오류", Toast.LENGTH_SHORT).show()
            }
        })
    }
}