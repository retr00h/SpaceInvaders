package com.devfabiocirelli.spaceinvaders

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class Field: View {

    private val paint = Paint()
    val TAG = "activity"
    lateinit var canvas: Canvas
    var fire: Boolean = false
    var playerShip: Player? = null
    var enemy: Enemy? = null
    var start = false
    var numEnemy = 0
    var points = 0
    var colpito = false

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

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        this.canvas = canvas

        if(playerShip == null) {
            playerShip = Player(context, width, height)
        }

        var i = 0

        if(enemy == null){
            enemy = Enemy(context, width, height)
            enemy!!.addEnemy(numEnemy)
        }

        //disegna tutti i nemici presenti in enemyList
        for(e: Bitmap in enemy!!.enemyList){
            paint.setColor(Color.TRANSPARENT)
            canvas.drawRect(enemy!!.enemyHitboxList[i], paint)
            paint.setColor(Color.BLACK)
            canvas.drawBitmap(e, (enemy!!.enemyHitboxList[i].left).toFloat(), (height/100).toFloat(), paint)
            i++
            invalidate()
        }

        //setta start a true dopo aver disegnato i nemici per animarli nel gameFragment
        start = true

        //Disegna il giocatore e la sua hitbox
        paint.setColor(Color.TRANSPARENT)
        canvas.drawRect(playerShip!!.playerHitBox, paint)
        paint.setColor(Color.BLACK)
        canvas.drawBitmap(playerShip!!.mShipBitmap, (playerShip!!.x).toFloat(), (playerShip!!.y).toFloat(), paint)

        //al primo ciclo dell'onDraw la variabile fire è settata a false, al click del bottone per sparare viene settata a true
        //quindi viene invalidato il canvas e quindi ridisegnato, a questo punto può entrare nell'if e disegnare il proiettile sparato
        //questo ciclo viene iterato finchè il proiettile o entra in collisione con un personaggio, o arriva alla fine dello schermo

        if(fire) {
            paint.setColor(playerShip!!.selectedShipColor)
            for(bullet: Rect in playerShip!!.bulletList) {
                var pos = 0
                for (enemyHitbox: Rect in enemy!!.enemyHitboxList) {
                    if (bullet.intersect(enemyHitbox)) {
                        points = 50
                        colpito = true
                        playerShip!!.compactBulletList(bullet)
                        enemy!!.compactEnemyList(enemyHitbox, pos)
                    } else {
                        canvas.drawRect(bullet, paint)
                    }
                    canvas.drawRect(bullet, paint)
                    pos++
                }
            }
                }

            }



    fun generateEnemy(n: Int){
        numEnemy = n
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


    //all'avvio del gioco i nemici si spostano verso destra
    var enemyPos = 1
    var direction = 1

    fun enemyUpdatePosition(){
            direction = enemy!!.updatePosition(enemyPos)
            if (direction >= width) {
                enemyPos = 0
            }
            if (direction <= 0) {
                if(enemy!!.noEnemy){
                    start = false
                } else {
                    enemyPos = 1
                }
            }
    }

    fun getEnemy(): Int{
        return enemy!!.getNumEnemy()
    }
}


