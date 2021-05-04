package com.devfabiocirelli.spaceinvaders

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class CustomPlayerShip(conteext: Context, width: Int, height: Int) {}
//
//    private val paint = Paint()
//    var ship: Int? = null
//
//    val width = width
//    val height = height
//
//        val mWidth = width.toFloat()
//        val mHeight = height.toFloat()
//
//
//        var left = width
//        var top = height
//
//        when(ship){
//            1 -> {  val bitmap = BitmapFactory.decodeResource(res, R.mipmap.ic_launcher) //TODO: SOSTITUIRE CON BITMAP CORRETTA
//                val mBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false)
//
//                val rect = Rect(0, 0, bitmap.width, bitmap.height)
//
//                paint.setColor(Color.RED)   //TODO: SETTARE IL COLORE TRASPARENTE
//                canvas.drawRect(rect, paint)
//                paint.setColor(Color.BLACK)
//                canvas.drawBitmap(mBitmap, 0f, 0f, paint)}
//
//            2 -> {  val bitmap = BitmapFactory.decodeResource(res, R.mipmap.ic_launcher)    //TODO: SOSTITUIRE CON BITMAP CORRETTA
//                val mBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false)
//                left = (width*0.2).toInt()
//                top = (height*0.5).toInt()
//
//                val rect = Rect(left, top, (bitmap.width*0.5).toInt(), (bitmap.height/2).toInt())
//
//                paint.setColor(Color.RED)       //TODO: SETTARE IL COLORE TRASPARENTE
//                canvas.drawRect(rect, paint)
//                paint.setColor(Color.BLACK)
//                canvas.drawBitmap(mBitmap, 0f, 0f, paint)}
//
//            3 -> {  val bitmap = BitmapFactory.decodeResource(res, R.mipmap.ic_launcher)  //TODO: SOSTITUIRE CON BITMAP CORRETTA
//                val mBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false)
//                left = (width*0.4).toInt()
//                top = (height*0.3).toInt()
//                val rectV = Rect(left, top, (bitmap.width*0.35).toInt(), (bitmap.height*0.5).toInt())
//                left = (width*0.2).toInt()
//                top = (height*0.5).toInt()
//                val rectO = Rect(left, top, (bitmap.width*0.5).toInt(), (bitmap.height*0.5).toInt())
//
//                paint.setColor(Color.RED)       //TODO: SETTARE IL COLORE TRASPARENTE
//                canvas.drawRect(rectV, paint)
//                canvas.drawRect(rectO, paint)
//                paint.setColor(Color.BLACK)
//                canvas.drawBitmap(mBitmap, 0f, 0f, paint)}
//            4 -> {
//
//            }
//            else -> {
//
//            }
//        }
//
//
//    }
//    fun choseShip(ship: Int) { this.ship = ship }
//}