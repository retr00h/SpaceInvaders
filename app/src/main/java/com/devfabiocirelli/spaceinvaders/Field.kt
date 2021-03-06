package com.devfabiocirelli.spaceinvaders

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.random.Random

class Field: View {
    private val paint = Paint()
    lateinit var canvas: Canvas
    //la variabile fire serve per far sapere al canvas se disegnare  o meno i proiettili sparati dal giocatore
    var fire: Boolean = false
    //istanza della classe Player, classe dedicata al giocatore
    var playerShip: Player? = null
    //istanza della classe Enemy dedicata ai nemici
    var enemy: Enemy? = null
    //la variabile start indica se il gioco sia portito o meno al thread lanciato dal GameFragment
    var start = false
    //colpito serve al thread lanciato da GameFragment in modo che se il giocatore
    //colpisce un nemico, il thread lo sappia e aggiorni il punteggio del giocatore
    var nemicoColpito = false
    var giocatoreColpito = false

    val dataBaseHelper = DataBaseHelper(context)
    val playerData = dataBaseHelper.readGameData()

    var playerLives = playerData.lives
    var numEnemy = playerData.enemies

    constructor(context: Context?) : super(context){
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        init(attrs)
    }

    private fun init(attrs: AttributeSet?){}

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        this.canvas = canvas

        if (playerShip == null) {
            playerShip = Player(context, width, height)
        }

        var i = 0

        if (enemy == null) {
            enemy = Enemy(context, width, height)
            enemy!!.addEnemy(numEnemy)

        }

        //disegna tutti i nemici presenti in enemyList
        for (e: Bitmap in enemy!!.enemyList) {
            paint.setColor(Color.TRANSPARENT)
            canvas.drawRect(enemy!!.enemyHitboxList[i], paint)
            paint.setColor(Color.BLACK)
            canvas.drawBitmap(
                e,
                (enemy!!.enemyHitboxList[i].left).toFloat(),
                (height / 100).toFloat(),
                paint
            )
            i++
        }
        invalidate()

        //setta start a true dopo aver disegnato i nemici per animarli nel gameFragment
        start = true

        //Disegna il giocatore e la sua hitbox
        paint.setColor(Color.TRANSPARENT)
        canvas.drawRect(playerShip!!.playerHitBox, paint)
        paint.setColor(Color.BLACK)
        canvas.drawBitmap(
            playerShip!!.mShipBitmap,
            (playerShip!!.x).toFloat(),
            (playerShip!!.y).toFloat(),
            paint
        )

        //se ci sono proiettili sparati dai nemici, vengono disegnati sul canvas
        if (enemy!!.bulletList.size > 0) {
            paint.setColor(Color.RED)
            for (bullet: Rect in enemy!!.bulletList) {
                if (bullet.intersect(playerShip!!.playerHitBox)) {
                    giocatoreColpito = true
                    playerLives -= 1
                    enemy!!.compactBulletList(bullet)
                } else {
                    canvas.drawRect(bullet, paint)
                }
            }
        }

        /*
        al primo ciclo dell'onDraw la variabile fire ?? settata a false, al click del bottone per sparare viene settata a true
        quindi viene invalidato il canvas e quindi ridisegnato, a questo punto pu?? entrare nell'if e disegnare il proiettile sparato
        questo ciclo viene iterato finch?? il proiettile o entra in collisione con un personaggio, o arriva alla fine dello schermo
         */
        try {
            if (fire) {
                paint.setColor(playerShip!!.bulletColor)
                for (bullet: Rect in playerShip!!.bulletList) {
                    var pos = 0
                    for (enemyHitbox: Rect in enemy!!.enemyHitboxList) {
                        if (bullet.intersect(enemyHitbox)) {
                            nemicoColpito = true
                            playerShip!!.compactBulletList(bullet)
                            enemy!!.compactEnemyList(enemyHitbox, pos)
                        } else {
                            canvas.drawRect(bullet, paint)
                        }
                        pos++
                    }
                }
            }

        } catch (e: ConcurrentModificationException){ }
    }

    /*
    metodi di gestione del movimento del giocatore,
    invocano dei metodi della classe Player
     */
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
        /*se p ?? maggiore di zero vuol dire che ci sono dei proiettili nella lista dei proiettili sparati dal giocatore
        e quindi dovranno essere diegnati sul canvas
         */
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

    //aggiunge un proiettile ogni voltache l'utente preme sul bottone per sparare
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

    //se il nemico si trova circa nella posizione del giocatore, randomicamente decide di sparare o meno
    fun enemyFire(i: Int, mainActivity: MainActivity){
        try {
            if (enemy!!.enemyHitboxList[i].left in playerShip!!.playerHitBox.left..playerShip!!.playerHitBox.right ||
                enemy!!.enemyHitboxList[i].right in playerShip!!.playerHitBox.left..playerShip!!.playerHitBox.right
            ) {
                if (Random.nextInt(0, 3) == 2) {
                    if (mainActivity.settings.vibrations) mainActivity.vibe.vibrate(80)

                    enemy!!.addBullet(i)
                }
            }

        } catch (e: IndexOutOfBoundsException){ }
        enemy!!.fire()
    }

}


