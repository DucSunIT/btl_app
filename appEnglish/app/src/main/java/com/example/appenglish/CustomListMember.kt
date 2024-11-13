package com.example.appenglish

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class CustomListMember(private val activity: Activity, private val list: List<ListMember>) :
    ArrayAdapter<ListMember>(activity, R.layout.custom_list_view) {

    override fun getCount(): Int {
        return list.size
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val contexts = activity.layoutInflater

        val rowView = contexts.inflate(R.layout.custom_list_view, parent, false)

        val imageUser = rowView.findViewById<ImageView>(R.id.imgUser)
        val nameMember = rowView.findViewById<TextView>(R.id.txtNameMember)
        val imageVip = rowView.findViewById<ImageView>(R.id.imgVip)

        imageUser.setImageResource(list[position].imageUser)
        nameMember.text = list[position].nameMember
        imageVip.setImageResource(list[position].imageVip)
        return rowView
    }
}