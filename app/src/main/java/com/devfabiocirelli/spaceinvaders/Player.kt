package com.devfabiocirelli.spaceinvaders

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.DrawableCompat

class Player(context: Context, width: Int, height: Int) {

    val h = height
    val w = width

    val context = context

    val dataBaseHelper = DataBaseHelper(context)

    var top = (height*0.2).toInt()
    var bottom = (height*0.3).toInt()
    var left = (width*0.4).toInt()
    var right = (height*0.3).toInt()
    var rect = Rect(left, top, right, bottom)
    var x = width/2
    var y = height/2

    var bl = 400
    var bt = top
    var br = 500
    var bb = 300
    var bullet = Rect(bl, bt, br,bb)

    val selectedCustom = dataBaseHelper.readCustomization()

    var selectedShipColor = when(selectedCustom.color) {
        0 -> R.color.green
        1 -> R.color.blue
        2 -> R.color.red
        3 -> R.color.grey
        4 -> R.color.darkGrey
        else -> R.color.white
    }

    private var selectedShip = when(selectedCustom.ship){
        0 -> R.drawable.ic_ship_1
        1 -> 2 //TODO: secondo modello
        2 -> 3 //TODO: terzo modello
        3 -> 4 //TODO: quarto modello
        else -> R.drawable.ic_ship_1
    }

    //TODO: controllare
    val ship = setShip(selectedShip, selectedShipColor)

    val bitmap = BitmapFactory.decodeResource(context.resources, ship)
    val mBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false)


    fun updatePositionPlayer(direction: Int){

        if(direction == 1){ //Se uguale a 1 si sposta a destra, altrimenti a sinistra

            top = top +350
            bottom= bottom +350
            left = left +350
            right = right +350
            x = left
            rect.top = top
            rect.bottom = bottom
            rect.left = left
            rect.right = right

        } else {

            top = top -350
            bottom= bottom -350
            left = left -350
            right = right -350
            x = left
            rect.top = top
            rect.bottom = bottom
            rect.left = left
            rect.right = right

        }

    }

    fun fire(): Int{

        bl += 1
        bt += 1
        br += 1
        bb += 1

        bullet.top = bt
        bullet.left = bl
        bullet.right = br
        bullet.bottom = bb

        return bt

    }


    //TODO: da controllare
    //Imposta il colore dell'astronave
    private fun setShip(selectedShip: Int, selectedColor: Int): Int{

        val drawable = AppCompatResources.getDrawable(context, selectedShip)
        //e' importante fare questa operazione per fornire la compatibilità con le versioni precedenti a API 22
        val wrappedDrawable = DrawableCompat.wrap(drawable!!)
        //imposta il colore
        DrawableCompat.setTint(wrappedDrawable, selectedColor)

        return selectedShip

    }

}

