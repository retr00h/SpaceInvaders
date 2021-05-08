package com.devfabiocirelli.spaceinvaders

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class Player(context: Context, width: Int, height: Int) {

    val h = height
    val w = width

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

    var selectedShip = when(ship){
        1 -> R.drawable.ic_ship_1
        2 -> 2 //TODO: secondo modello
        3 -> 3 //TODO: terzo modello
        4 -> 4 //TODO: quarto modello
        else -> R.drawable.ic_ship_1
    }


    val bitmap = BitmapFactory.decodeResource(context.resources, selectedShip)
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

    //Simile ad un metodo static di java
    //sia la variabile ship sia la funzione choseShip(), possono essere invocati
    //al di fuori della classe Player tramite Player.ship e Player.choseShip()
    companion object {
        var ship: Int? = null
        fun choseShip(ship: Int) { this.ship = ship }
    }

}

