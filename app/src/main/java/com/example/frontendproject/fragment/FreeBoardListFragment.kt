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
import com.example.frontendproject.model.FreeboardsResponse
import com.example.frontendproject.network.ApiService
import com.example.frontendproject.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FreeBoardListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView

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

        val api = RetrofitClient.retrofit.create(ApiService::class.java)
        api.getFreeBoardList().enqueue(object : Callback<List<FreeboardsResponse>> {
            override fun onResponse(
                call: Call<List<FreeboardsResponse>>,
                response: Response<List<FreeboardsResponse>>
            ) {
                if (response.isSuccessful) {
                    val freeBoardList = response.body() ?: emptyList()
                    recyclerView.adapter = FreeBoardAdapter(freeBoardList) { selectedFreeBoard ->
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
                }
            }

            override fun onFailure(call: Call<List<FreeboardsResponse>>, t: Throwable) {
                Toast.makeText(requireContext(), "서버 연결 실패", Toast.LENGTH_SHORT).show()
            }
        })

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_free_board, menu)

        val searchItem = menu.findItem(R.id.freeBoardSearch)
        val searchView = searchItem.actionView as SearchView

        searchView.queryHint = "제목으로 검색"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }
}