package com.devfabiocirelli.spaceinvaders

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.properties.Delegates

class Field: View {

    private val paint = Paint()
    val TAG = "activity"
    lateinit var canvas: Canvas
    var fire: Boolean = false
    var playerShip: Player? = null
    var enemy: Enemy? = null
    var start = false

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

    var i: Int = 0

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        this.canvas = canvas

        if(playerShip == null) {
            playerShip = Player(context, width, height)
        }


        if(enemy == null){
            enemy = Enemy(context, width, height)
            enemy!!.addEnemy(5)
        }

        paint.setColor(Color.RED)



        for(e: Bitmap in enemy!!.enemyList){
            canvas.drawRect(enemy!!.enemyHitboxList[i], paint)
            canvas.drawBitmap(e, (enemy!!.enemyHitboxList[i].left).toFloat(), (height/100).toFloat(), paint)
            i++
            invalidate()
        }

        //setta start a true dopo aver disegnato i nemici per animarli nel gameFragment
        start = true
        i = 0


        paint.setColor(Color.WHITE)
        canvas.drawRect(playerShip!!.playerHitBox, paint)
        paint.setColor(Color.BLACK)
        canvas.drawBitmap(playerShip!!.mShipBitmap, (playerShip!!.x).toFloat(), (playerShip!!.y).toFloat(), paint)

        //al primo ciclo dell'onDraw la variabile fire è settata a false, al click del bottone per sparare viene settata a true
        //quindi viene invalidato il canvas e quindi ridisegnato, a questo punto può entrare nell'if e disegnare il proiettile sparato
        //questo ciclo viene iterato finchè il proiettile o entra in collisione con un personaggio, o arriva alla fine dello schermo


        if(fire) {
            paint.setColor(Color.RED)
            for(bullet: Rect in playerShip!!.bulletList) {
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





    fun onClickUpdateRight(){
        playerShip?.updatePlayerPosition(1)
        invalidate()
    }


    fun onClickUpdateLeft(){
        playerShip?.updatePlayerPosition(0)
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

    var enemyPos = 1
    fun enemyUpdatePosition(){
        enemyPos = enemy!!.updatePosition(enemyPos)
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

