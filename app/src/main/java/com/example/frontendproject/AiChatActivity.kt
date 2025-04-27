package com.example.frontendproject

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.frontendproject.adapter.ChatAdapter
import com.example.frontendproject.databinding.ActivityAiChatBinding
import com.example.frontendproject.model.AiChatRequest
import com.example.frontendproject.model.AiChatResponse
import com.example.frontendproject.model.Message
import com.example.frontendproject.network.ApiService
import com.example.frontendproject.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AiChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAiChatBinding
    private lateinit var chatAdapter: ChatAdapter
    private val messageList = mutableListOf<Message>()

    private val api = RetrofitClient.retrofit.create(ApiService::class.java)
    private var memberId = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAiChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        memberId = getSharedPreferences("user_prefs", MODE_PRIVATE).getLong("memberId", -1)
        setupRecyclerView()
        loadChatHistory()
        setupSendButton()
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter(messageList)
        binding.chatRecyclerView.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(this@AiChatActivity)
        }
    }

    private fun loadChatHistory() {
        api.getAiChat(memberId).enqueue(object : Callback<List<AiChatResponse>> {
            override fun onResponse(
                call: Call<List<AiChatResponse>>,
                response: Response<List<AiChatResponse>>
            ) {
                if (response.isSuccessful) {
                    val chatHistory = response.body()
                    if (!chatHistory.isNullOrEmpty()) {
                        for (chat in chatHistory) {
                            messageList.add(Message(chat.content, isUser = if (chat.sender.equals("USER")) {
                                true
                            } else {
                                false
                            }))
                        }
                        chatAdapter.notifyDataSetChanged()
                        binding.chatRecyclerView.scrollToPosition(messageList.size - 1)
                    }
                } else {
                    Toast.makeText(this@AiChatActivity, "기존 대화 불러오기 실패", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<AiChatResponse>>, t: Throwable) {
                Toast.makeText(this@AiChatActivity, "서버 통신 오류", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupSendButton() {
        binding.chatSubmitBtn.setOnClickListener {
            Toast.makeText(this, "서버에 데이터 전송중 잠시만 기다려주세요", Toast.LENGTH_SHORT).show()
            val userMessage = binding.chatContentEditText.text.toString()
            if (userMessage.isNotEmpty()) {
                messageList.add(Message(userMessage, isUser = true))
                chatAdapter.notifyItemInserted(messageList.size - 1)
                binding.chatRecyclerView.scrollToPosition(messageList.size - 1)

                binding.chatContentEditText.text.clear()

                sendMessageToServer(userMessage)
            } else {
                Toast.makeText(this, "메시지를 입력하세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendMessageToServer(userInput: String) {
        lateinit var aiReply: String
        val data = AiChatRequest(question = userInput)
        api.addAiChat(memberId, data).enqueue(object: Callback<String> {
            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                if (response.isSuccessful) {
                    aiReply = response.body().toString()
                    Toast.makeText(this@AiChatActivity, "서버에서 성공적으로 데이터를 받아왔습니다", Toast.LENGTH_SHORT).show()
                    messageList.add(Message(aiReply, isUser = false))
                    chatAdapter.notifyItemInserted(messageList.size - 1)
                    binding.chatRecyclerView.scrollToPosition(messageList.size - 1)
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(this@AiChatActivity, "서버 통신 오류", Toast.LENGTH_SHORT).show()
            }
        })
    }
}