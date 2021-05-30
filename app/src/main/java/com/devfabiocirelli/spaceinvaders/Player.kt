package com.devfabiocirelli.spaceinvaders

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.Log

class Player(context: Context, width: Int, height: Int) {

    val length = width

    val h = height/4
    val w = width/10

    var x = (width*0.45).toInt()
    var y = (height*0.75).toInt()

    val shipSpeed = width/300

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
        0 -> R.mipmap.ic_playership_1_foreground
        1 -> R.mipmap.ic_playership_2_foreground
        2 -> R.mipmap.ic_playership_3_foreground
        3 -> R.mipmap.ic_playership_4_foreground
        else -> R.mipmap.ic_playership_1_foreground
    }

    //TODO: controllare
    val ship = setShip(selectedShip, selectedShipColor)

    val shipBitmap =  BitmapFactory.decodeResource(context.resources, ship)
    val mShipBitmap =  Bitmap.createScaledBitmap(shipBitmap, h, w, false)

    //Le seguenti coordinate vengono utilizzate anche per i proiettili
    var top = y + (mShipBitmap.height*0.3).toInt()
    var bottom= y + (mShipBitmap.height*0.8).toInt()
    val left = x + (mShipBitmap.width*0.2).toInt()
    val right = x + (mShipBitmap.width*0.8).toInt()

    var playerHitBox = Rect(left, top, right, bottom)

    var bLeft = x + (mShipBitmap.width*0.4).toInt()
    var bRight = x + (mShipBitmap.width*0.6).toInt()


    fun updatePlayerPosition(direction: Int) {

        if (right <= length && left >= 0) {
            if (direction == 1) { //Se uguale a 1 si sposta a destra, altrimenti a sinistra
                x += shipSpeed
            } else {
                x -= shipSpeed
            }

            //aggiornamento coordinate hitbox giocatore
            playerHitBox.left = x + (mShipBitmap.width*0.2).toInt()
            playerHitBox.right = x + (mShipBitmap.width*0.8).toInt()
            //aggiornamento coordinate proiettile
            bLeft = x + (mShipBitmap.width*0.4).toInt()
            bRight = x + (mShipBitmap.width*0.6).toInt()

        } else {
            Log.i("Player", "Fine mappa")
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

        return selectedShip

    }

    fun compactBulletList(bullet: Rect){
        var tempBulletList = mutableListOf<Rect>()
        for(r: Rect in bulletList){
            if(r != bullet){
                tempBulletList.add(r)
            }
        }
        bulletList = tempBulletList
    }

}

