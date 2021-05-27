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

    val length = width

    val h = height/4
    val w = width/10

    var x = (width*0.45).toInt()
    var y = (height*0.75).toInt()

    val shipSpeed = width/10

    val context = context

    val dataBaseHelper = DataBaseHelper(context)

//    var top = (height*0.2).toInt()
//    var bottom = (height*0.3).toInt()
//    var left = (width*0.4).toInt()
//    var right = (height*0.3).toInt()
//    var rect = Rect(left, top, right, bottom)
//
//
//    var bl = 400
//    var bt = top
//    var br = 500
//    var bb = 300
//    var bullet = Rect(bl, bt, br,bb)

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

    val bitmapWidth = mBitmap.width
    val bitmapHeight = mBitmap.height

    //Le seguenti coordinate vengono utilizzate anche per i proiettili
    var top = y + (bitmapHeight*0.3).toInt()
    var bottom= y + (bitmapHeight*0.8).toInt()
    val left = x + (bitmapWidth*0.2).toInt()
    val right = x + (bitmapWidth*0.8).toInt()

    var playerHitBox = Rect(left, top, right, bottom)

    var bLeft = x + (bitmapWidth*0.4).toInt()
    var bRight = x + (bitmapWidth*0.6).toInt()


    fun updatePlayerPosition(direction: Int) {

        if (right <= length || left >= 0) {
            if (direction == 1) { //Se uguale a 1 si sposta a destra, altrimenti a sinistra
                x += shipSpeed
            } else {
                x -= shipSpeed
            }

            //aggiornamento coordinate hitbox giocatore
            playerHitBox.left = x + (bitmapWidth*0.2).toInt()
            playerHitBox.right = x + (bitmapWidth*0.8).toInt()
            //aggiornamento coordinate proiettile
            bLeft = x + (bitmapWidth*0.4).toInt()
            bRight = x + (bitmapWidth*0.6).toInt()

        }
    }

    var bulletList = mutableListOf<Rect>()

    fun addBullet(){
        bulletList.add(Rect(bLeft,top,bRight,bottom))
    }

    fun fire(): Int{

        var iterator = bulletList.iterator()
        while(iterator.hasNext()){
            val item = iterator.next()
            var fine = fireUpdate(item)
            if(fine <= 0){
                iterator.remove()
            }
        }
        return bulletList.size
    }

    private fun fireUpdate(bullet: Rect): Int{

        bullet.top -= 20
        bullet.bottom -= 20

        return bullet.bottom

    }


    //TODO: da controllare
    //Imposta il colore dell'astronave
    private fun setShip(selectedShip: Int, selectedColor: Int): Int{

//        val drawable = AppCompatResources.getDrawable(context, selectedShip)
//        //e' importante fare questa operazione per fornire la compatibilità con le versioni precedenti a API 22
//        val wrappedDrawable = DrawableCompat.wrap(drawable!!)
//        //imposta il colore
//        DrawableCompat.setTint(wrappedDrawable, selectedColor)

        return selectedShip

    }

}

