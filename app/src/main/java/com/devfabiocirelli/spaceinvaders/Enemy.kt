package com.devfabiocirelli.spaceinvaders

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect

class Enemy(context: Context, width: Int, height: Int) {

    val length = width

    val h = height/4
    val w = width/10

    var x = (width*0.45).toInt()
    var y = (height*0.75).toInt()

    val shipSpeed = width/10

    val context = context

    var enemyList = mutableListOf<Bitmap>()
    var enemyHitboxList = mutableListOf<Rect>()

    val bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.ic_playership_1_foreground)
    var left = bitmap.width
    val top = bitmap.height
    var right = bitmap.width
    val bottom = bitmap.height
    var enemyHitbox = Rect(left, top, right, bottom)

    fun addEnemy(number: Int){
        for(i in 1..number) {
            enemyList.add(bitmap)
            enemyHitbox = Rect(left, top, right, bottom)
            enemyHitboxList.add(enemyHitbox)
            setNextPosition()
        }
    }

    fun setNextPosition(){
        left += (w*1.3).toInt()
        right += (w*1.3).toInt()
    }

    fun updatePosition(direction: Int) : Int{
        if(direction == 1){
            for(h: Rect in enemyHitboxList){
                h.left += (w*0.2).toInt()
                h.right += (w*0.2).toInt()
            }
            return enemyHitboxList[enemyHitboxList.size].right
        } else {
            for(h: Rect in enemyHitboxList){
                h.left -= (w*0.2).toInt()
                h.right -= (w*0.2).toInt()
            }
            return enemyHitboxList[0].left
        }



    }

}