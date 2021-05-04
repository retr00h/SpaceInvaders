package com.devfabiocirelli.spaceinvaders

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView

class MyAdapter(private val context: Context, val data: Array<Int>, val layout: Int) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var newView = convertView

        if (convertView == null) {
            newView = LayoutInflater.from(context).inflate(layout, parent, false)

        }
        //In base al layout imposta la view
        if (newView != null) {

            if(layout == R.layout.ship_color_view) {
                val imageColor = newView.findViewById<customColorView>(R.id.colorView)
                imageColor.setColor(data[position])
                imageColor.setOnClickListener{

                }
            }
            if(layout == R.layout.ship_model_view){
                val shipModel = newView.findViewById<ImageView>(R.id.shipImage)
                shipModel.setImageResource(data[position])

            }
        }

        return newView!!


    }



    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return data.size
    }

}