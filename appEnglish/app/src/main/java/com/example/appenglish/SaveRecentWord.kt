package com.example.appenglish

import android.os.Parcel
import android.os.Parcelable

class SaveRecentWord(
    var word: String,
    var ipa: String,
    var img: Int,
    var imgDelete: Int,
    var type: String,
    var defi: String)
//) : Parcelable {
//    constructor(parcel: Parcel) : this(
//        parcel.readString() ?: "",
//        parcel.readString() ?: "",
//        parcel.readInt(),
//        parcel.readString() ?: "",
//        parcel.readString() ?: ""
//    )
//
//    override fun describeContents(): Int = 0
//
//
//    override fun writeToParcel(dest: Parcel, flags: Int) {
//        dest.writeString(word)
//        dest.writeString(ipa)
//        dest.writeInt(img)
//        dest.writeString(type)
//        dest.writeString(defi)
//    }
//
//    companion object CREATOR : Parcelable.Creator<SaveRecentWord> {
//        override fun createFromParcel(parcel: Parcel): SaveRecentWord = SaveRecentWord(parcel)
//        override fun newArray(size: Int): Array<SaveRecentWord?> = arrayOfNulls(size)
//    }
