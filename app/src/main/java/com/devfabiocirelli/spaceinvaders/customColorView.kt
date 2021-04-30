package com.devfabiocirelli.spaceinvaders

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class customColorView : View{

    private val paint = Paint()

    constructor(context: Context?) : super(context){

    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){

    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){

    }
    //per modificare il colore della custom view
    fun setColor(i: Int){
        paint.setColor(getResources().getColor(i))
        invalidate()
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawCircle(width.toFloat()/2, height.toFloat()/2, (height.toFloat())*0.3.toFloat(), paint)
    }
}