package com.devfabiocirelli.spaceinvaders

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_customization.*

class CustomizationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customization)

        //array delle immagini delle navi <da modificare>
        val ship = arrayOf(
            R.mipmap.ic_launcher, R.mipmap.ic_launcher_round, R.mipmap.ic_launcher_round, R.mipmap.ic_launcher, R.mipmap.ic_launcher_round, R.mipmap.ic_launcher_round
        )

        //array di colori <da modificare>
        val colors = arrayOf(
            R.color.green, R.color.blue, R.color.red, R.color.grey, R.color.white, R.color.darkGrey
        )
        //Popola le listView con le immagini e i colori disponibili
        list_view_ship.adapter = MyAdapter(this, ship, R.layout.ship_model_view)
        list_view_color.adapter = MyAdapter(this, colors, R.layout.ship_color_view)

    }
}