package com.example.frontendproject.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.frontendproject.databinding.ItemBotMessageBinding
import com.example.frontendproject.databinding.ItemUserMessageBinding
import com.example.frontendproject.model.Message

class UserMessageViewHolder(val binding: ItemUserMessageBinding) : RecyclerView.ViewHolder(binding.root)

class AiMessageViewHolder(val binding: ItemBotMessageBinding) : RecyclerView.ViewHolder(binding.root)

class ChatAdapter(
    val messageList: List<Message>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_USER = 1
        private const val VIEW_TYPE_AI = 2
    }

    override fun getItemCount(): Int = messageList.size

    override fun getItemViewType(position: Int): Int {
        return if (messageList[position].isUser) VIEW_TYPE_USER else VIEW_TYPE_AI
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_USER) {
            UserMessageViewHolder(
                ItemUserMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        } else {
            AiMessageViewHolder(
                ItemBotMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messageList[position]

        if (holder is UserMessageViewHolder) {
            holder.binding.userMessageText.text = message.content
        } else if (holder is AiMessageViewHolder) {
            holder.binding.aiMessageText.text = message.content
        }
    }
}
