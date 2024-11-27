package com.example.appenglish

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RvAdapter(
    private var suggestions: List<String>,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<RvAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_suggest, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rawText = suggestions[position]

        // Kiểm tra và đảm bảo rawText hợp lệ trước khi sử dụng
        val word = if (rawText.isNotEmpty()) rawText.split(" ")[0] else ""
        holder.textView.text = rawText

        // Đảm bảo chỉ gọi onClick khi từ không rỗng
        holder.itemView.setOnClickListener {
            if (word.isNotEmpty()) {
                onClick(word)
            }
        }
    }

    override fun getItemCount(): Int = suggestions.size

    fun updateData(newSuggestions: List<String>) {
        // Cập nhật dữ liệu mới cho adapter
        suggestions = newSuggestions
        notifyDataSetChanged()
    }
}
