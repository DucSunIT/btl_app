package custom_ui

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.appenglish.R
import data_class.Vocabulary

class CustomGridVoca(private val activity: Activity, private val list: List<Vocabulary>) :
    ArrayAdapter<Vocabulary>(activity, R.layout.custom_voca) {
    override fun getCount(): Int {
        return list.size
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val contexts = activity.layoutInflater

        val rowView = contexts.inflate(R.layout.custom_voca, parent, false)

        val images = rowView.findViewById<ImageView>(R.id.imgVS)
        val nameSubject = rowView.findViewById<TextView>(R.id.txtNS)

        images.setImageResource(list[position].images)
        nameSubject.text = list[position].nameSubject
        return rowView
    }
}