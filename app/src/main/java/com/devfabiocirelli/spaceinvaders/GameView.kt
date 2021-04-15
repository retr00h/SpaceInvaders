package com.devfabiocirelli.spaceinvaders

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View

class GameView : View {
    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

    }
}