package com.devfabiocirelli.spaceinvaders

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.util.Log
import kotlin.random.Random

class Enemy(context: Context, val width: Int, val height: Int) {

    val h = height/4
    val w = width/10

    var x = 0
    val enemyShipSpeed = (w * 0.2).toInt()

    val context = context

    var enemyList = mutableListOf<Bitmap>()
    var enemyHitboxList = mutableListOf<Rect>()

    val bitmap = BitmapFactory.decodeResource(context.resources, R.mipmap.ic_enemy_1_foreground)
    val mBitmap = Bitmap.createScaledBitmap(bitmap, h, w, false)

    var bLeft = x + (mBitmap.width*0.45).toInt()
    var bRight = x + (mBitmap.width*0.55).toInt()

    var left = (mBitmap.width*0.1).toInt()
    val top = (mBitmap.height*0.3).toInt()
    var right = (mBitmap.width*0.9).toInt()
    val bottom = (mBitmap.height*0.8).toInt()
    var enemyHitbox = Rect(left, top, right, bottom)
    var noEnemy = false

    fun addEnemy(number: Int){
        for(i in 1..number) {
            enemyList.add(randomEnemy())
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
                x += enemyShipSpeed

                //aggiornamento coordinate proiettile
                bLeft = x + (mBitmap.width*0.45).toInt()
                bRight = x + (mBitmap.width*0.55).toInt()

                for (h: Rect in enemyHitboxList) {
                    h.left += enemyShipSpeed
                    h.right += enemyShipSpeed
                }
                if(enemyHitboxList.size-1 < 0){
                    noEnemy = true
                    return -1
                } else{
                    return enemyHitboxList[enemyHitboxList.size-1].right
                }

            } else {
                x -= enemyShipSpeed

                //aggiornamento coordinate proiettile
                bLeft = x + (mBitmap.width*0.45).toInt()
                bRight = x + (mBitmap.width*0.55).toInt()

                for (h: Rect in enemyHitboxList) {
                    h.left -= enemyShipSpeed
                    h.right -= enemyShipSpeed
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

    //Metodi gestione proiettili dei nemici

    var bulletList = mutableListOf<Rect>()

    //come argomento ricevo un int per idicare quale dei nemici ha sparato per ottenere le sue coordinate e disegnare il rpoiettile
    //in modo che parta dal nemico che lo ha sparato
    fun addBullet(i: Int){
        if(i < enemyHitboxList.size) {
            var enemy = enemyHitboxList[i]
            var bullet = Rect((enemy.left + (mBitmap.width*0.45)).toInt(), enemy.top, (enemy.right - (mBitmap.width*0.45)).toInt(), enemy.bottom)
            bulletList.add(bullet)
        }
    }

    private fun updateBulletPosition(bullet: Rect): Int{
        bullet.top += 20
        bullet.bottom += 20

        return bullet.bottom
    }

    fun fire(){
        var iterator = bulletList.iterator()
        while(iterator.hasNext()){
            val item = iterator.next()
            var fine = updateBulletPosition(item)
            if(fine >= height){
                iterator.remove()
            }
        }
    }

    private fun randomEnemy(): Bitmap{

        var enemyShipImage = when(Random.nextInt(1,3)){
            1 -> R.mipmap.ic_enemy_2_foreground
            2 -> R.mipmap.ic_enemy_3_foreground
            else -> R.mipmap.ic_enemy_1_foreground
        }

        val bitmap = BitmapFactory.decodeResource(context.resources, enemyShipImage)
        val mBitmap = Bitmap.createScaledBitmap(bitmap, h, w, false)

        return mBitmap
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