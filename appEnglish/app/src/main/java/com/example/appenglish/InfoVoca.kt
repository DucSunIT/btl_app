package com.example.appenglish
/*
* Class dùng để lưu trữ thông tin của một item trong gridView từ vựng*/
data class InfoVoca(
    val imgRes: Int,
    val ipa: String,
    val word: String,
    val imageDetails: Int,
    val imageSound: Int
) {
}