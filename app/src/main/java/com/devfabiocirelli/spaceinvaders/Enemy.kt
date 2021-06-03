package com.devfabiocirelli.spaceinvaders

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect

class Enemy(context: Context, width: Int, height: Int) {

    val h = height/4
    val w = width/10

    val context = context

    var enemyList = mutableListOf<Bitmap>()
    var enemyHitboxList = mutableListOf<Rect>()

    val bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.ic_playership_1_foreground)
    val mBitmap = Bitmap.createScaledBitmap(bitmap, h, w, false)
    var left = (mBitmap.width*0.1).toInt()
    val top = (mBitmap.height*0.3).toInt()
    var right = (mBitmap.width*0.9).toInt()
    val bottom = (mBitmap.height*0.8).toInt()
    var enemyHitbox = Rect(left, top, right, bottom)
    var noEnemy = false

    fun addEnemy(number: Int){
        for(i in 1..number) {
            enemyList.add(mBitmap)
            enemyHitbox = Rect(left, top, right, bottom)
            enemyHitboxList.add(enemyHitbox)
            setNextPosition()
        }
    }

    fun setNextPosition(){
        left += (w*1.3).toInt()
        right += (w*1.3).toInt()
    }

    fun updatePosition(direction: Int): Int {
            if (direction == 1) {
                for (h: Rect in enemyHitboxList) {
                    h.left += (w * 0.2).toInt()
                    h.right += (w * 0.2).toInt()
                }
                if(enemyHitboxList.size-1 < 0){
                    noEnemy = true
                    return -1
                } else{
                    return enemyHitboxList[enemyHitboxList.size-1].right
                }

            } else {
                for (h: Rect in enemyHitboxList) {
                    h.left -= (w * 0.2).toInt()
                    h.right -= (w * 0.2).toInt()
                }
                if(enemyHitboxList.size-1 < 0) {
                    noEnemy = true
                    return -1
                } else {
                    return enemyHitboxList[0].left
                }
            }
    }

    fun compactEnemyList(enemyHitbox: Rect, enemyPosInList: Int){
        var tempHitboxList = mutableListOf<Rect>()
        var tempEnemyList = mutableListOf<Bitmap>()
        var i = 0
        for(rect: Rect in enemyHitboxList){
            if(rect != enemyHitbox && i != enemyPosInList){
                tempHitboxList.add(rect)
                tempEnemyList.add(enemyList[i])
            }
            i++
        }
        enemyList = tempEnemyList
        enemyHitboxList = tempHitboxList
    }

    fun getNumEnemy(): Int{
        return enemyList.size
    }

}