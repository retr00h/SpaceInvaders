package com.devfabiocirelli.spaceinvaders

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View

class Field: View {

    private val paint = Paint()
    val TAG = "activity"
    var canvas: Canvas? = null
    var fire: Boolean = false
    var playerShip: Player? = null

    constructor(context: Context?) : super(context){
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        init(attrs)
    }

    private fun init(attrs: AttributeSet?){

        setOnClickListener{
            invalidate()
        }

    }
    var colpito = false

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        this.canvas = canvas

        val mWidth = width.toFloat()
        val mHeight = height.toFloat()

        if(playerShip == null) {
            playerShip = Player(context, width, height)
        }

        val res: Resources = resources
        val ship = choseShip(4)
        var left = width
        var top = height



        when(ship){
//            1 -> {  val bitmap = BitmapFactory.decodeResource(res, R.mipmap.ic_playership_foreground)
//                    val mBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false)
//
//                    val rect = Rect(0, 0, bitmap.width, bitmap.height)
//
//                    paint.setColor(Color.RED)
//                    canvas.drawRect(rect, paint)
//                    paint.setColor(Color.BLACK)
//                    canvas.drawBitmap(mBitmap, 0f, 0f, paint)}
//
//            2 -> {  val bitmap = BitmapFactory.decodeResource(res, R.mipmap.ic_playership2_foreground)
//                    val mBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false)
//                    left = (width*0.2).toInt()
//                    top = (height*0.5).toInt()
//
//                    val rect = Rect(left, top, (bitmap.width*0.5).toInt(), (bitmap.height/2).toInt())
//
//                    paint.setColor(Color.RED)
//                    canvas.drawRect(rect, paint)
//                    paint.setColor(Color.BLACK)
//                    canvas.drawBitmap(mBitmap, 0f, 0f, paint)}
//
//            3 -> {  val bitmap = BitmapFactory.decodeResource(res, R.mipmap.ic_playership3_foreground)
//                    val mBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false)
//                    left = (width*0.4).toInt()
//                    top = (height*0.3).toInt()
//                    val rectV = Rect(left, top, (bitmap.width*0.35).toInt(), (bitmap.height*0.5).toInt())
//                    left = (width*0.2).toInt()
//                    top = (height*0.5).toInt()
//                    val rectO = Rect(left, top, (bitmap.width*0.5).toInt(), (bitmap.height*0.5).toInt())
//
//                    paint.setColor(Color.RED)
//                    canvas.drawRect(rectV, paint)
//                    canvas.drawRect(rectO, paint)
//                    paint.setColor(Color.BLACK)
//                    canvas.drawBitmap(mBitmap, 0f, 0f, paint)}
            4 -> {

                paint.setColor(Color.TRANSPARENT)
                canvas.drawRect(playerShip!!.playerHitBox, paint)
                paint.setColor(Color.BLACK)
                canvas.drawBitmap(playerShip!!.mBitmap, (playerShip!!.x).toFloat(), (playerShip!!.y).toFloat(), paint)

                //al primo ciclo dell'onDraw la variabile fire è settata a false, al click del bottone per sparare viene settata a true
                //quindi viene invalidato il canvas e quindi ridisegnato, a questo punto può entrare nell'if e disegnare il proiettile sparato
                //questo ciclo viene iterato finchè il proiettile o entra in collisione con un personaggio, o arriva alla fine dello schermo


                if(fire) {
                    Log.i(TAG, "FIREEEE")
                    paint.setColor(Color.RED)
                    for(bullet: Rect in playerShip!!.bulletList) {
                        Log.i(TAG, "${bullet}")
//                         if(bullet.intersect(r)){
//                             playerShip!!.bulletList.remove(bullet)
//                             colpito = true
//                         } else {
//                             canvas.drawRect(bullet, paint)
//                         }
                        canvas.drawRect(bullet, paint)
                    }
                }

            }

        }

    }
    fun choseShip(ship: Int) : Int {
        val selectedShip = ship;
        return selectedShip
    }

    fun onClickUpdateRight(){
        playerShip?.updatePlayerPosition(1)
        Log.i(TAG, "ciaooooooo${width}")
        invalidate()
    }


    fun onClickUpdateLeft(){
        playerShip?.updatePlayerPosition(0)
        Log.i(TAG, "ciaooooooo ${playerShip?.playerHitBox?.top}")
        invalidate()
    }

    var p = 2

    fun onClickFire(): Int{
        if (p > 0) {
            fire = true
            p = playerShip!!.fire()
            Log.i(TAG, "Sparato ${p} ${height}")
            return 1
        } else {
            fire = false
            p = 2
            invalidate()
            return 0
        }
    }

    fun onClickAddBullet(){
        playerShip!!.addBullet()
    }

}
//TODO: spostare questa parte di codice nel main
//thread(start = true){
//    while(true){
//        //Se il giocatore ha sparato, entra nell'if e chiede alla view di ridisegnarsi ogni 10 millisecondi
//        if(fire) {
//            var fine = playerShip.onClickFire()
//            playerShip.invalidate()
//            Thread.sleep(10)
//            if(fine == 0){
//                fire = false
//            }
//        }
//    }
//}

