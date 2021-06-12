package com.devfabiocirelli.spaceinvaders

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_game.*
import kotlin.concurrent.thread
import kotlin.random.Random

class GameFragment(val mainActivity: MainActivity) : Fragment() {

    lateinit var rightButton: Button
    lateinit var leftButton: Button
    lateinit var fireButton: Button
    lateinit var rootView: View
    lateinit var levelText: TextView
    lateinit var livesTextView: TextView
    lateinit var scoreText: TextView
    var fire = false
    var wichLevel = 0
    var numEnemies = 0
    var lives = 0
    var score = 0
    var bloccaThread = false
    var alreadyClicked = false
    var fireCicle = 9
    var enemySpeed = 2

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        rootView = inflater.inflate(R.layout.fragment_game, container, false)
        rightButton = rootView.findViewById(R.id.rightButton)
        leftButton = rootView.findViewById(R.id.leftButton)
        fireButton = rootView.findViewById(R.id.fireButton)
        levelText = rootView.findViewById(R.id.levelIntView)
        livesTextView = rootView.findViewById(R.id.livesInt)
        scoreText = rootView.findViewById(R.id.scoreIntView)

        val gameData = mainActivity.dataBaseHelper.readGameData()

        wichLevel = gameData.level
        lives = gameData.lives
        score = gameData.score

        levelText.setText("${mainActivity.applicationContext.getString(R.string.LevelText)}: ${wichLevel}")
        livesTextView.setText("${mainActivity.applicationContext.getString(R.string.livesText)}: ${lives}")
        scoreText.setText("${mainActivity.applicationContext.getString(R.string.scoreText)}: ${score}")

        rightButton.setOnClickListener{
            gameField.onClickUpdateRight()
        }

        leftButton.setOnClickListener{
            gameField.onClickUpdateLeft()
        }

        fireButton.setOnClickListener{
              if(!alreadyClicked) {
                  alreadyClicked = true
                  gameField.onClickAddBullet()
                  fire = true
                  gameField.onClickFire()
              }

        }

        thread(start = true) {
            var timing = 0
            val random = Random
            bloccaThread = false
            while (true) {
                //Se il giocatore ha sparato, entra nell'if e chiede alla view di ridisegnarsi ogni 100 millisecondi
                    try {
                        if(gameField.giocatoreColpito){
                            lives = gameField.playerLives
                            gameField.giocatoreColpito = false
                            setNewPlayerLives(lives)
                            if(gameField.playerLives <= 0){
                                mainActivity.gameOverFragment()
                            break
                            }
                        }
                        //termina il thread se bloccaThread == true
                        if(bloccaThread){
                            break
                        }
                        if (fire) {
                                var fine = gameField.onClickFire()
                                gameField.invalidate()
                                if (fine == 0) {
                                    fire = false
                                }
                        }
                        timing++
                        /*
                        Il seguente if setta un cooldown in modo che il giocatore possa sparare un proiettile
                        ogni 0,9 secondi (dopo che ha premuto il tasto per sparare, deve atendere 9 cicli del thread
                        prima di poter sparare nuovamente)
                         */
                        if(timing % fireCicle == 0 || timing >= 10){
                            alreadyClicked = false
                            timing = 0
                        }
                        /*
                        la variabile timing serve per dare "velocita'" diverse agli elementi presenti sul canvas,
                        timing % 2 == 0 ridisegna i nemici ogni due cicli (mentre i proiettili vengono ridisegnati ad ogni ciclo)
                         */
                        if (timing % enemySpeed == 0) {
                            if (gameField.start) {
                                gameField.enemyUpdatePosition()
                            }

                        }

                        //quando il giocatore colpisce un nemico, aggiorna il punteggio tramite setNewScore()
                        if(gameField.nemicoColpito) {
                            gameField.nemicoColpito = false
                            score += 50
                            setNewScore(score)
                        }

                        //se il gioco è partito, i nemici sparano
                        if(gameField.start){
                            gameField.enemyFire(random.nextInt(0, gameField.numEnemy))
                        }

                        Thread.sleep(100)

                        if(gameField.getEnemy() <= 0){
                            score += 50
                            setNewScore(score)
                            mainActivity.scoreFragment()
                            break
                        }

                    } catch (e :NullPointerException) {
                        break
                    }


            }
        }


        return rootView



    }

    override fun onPause() {
        super.onPause()
        bloccaThread = true
        mainActivity.dataBaseHelper.updateGameData(score, lives, gameField.getEnemy(), wichLevel, 1)

    }

    override fun onResume() {
        super.onResume()
        bloccaThread = false

    }


    private fun setNewScore(newScore: Int){
        scoreText.post {
            scoreText.setText("${mainActivity.applicationContext.getString(R.string.scoreText)}: ${newScore}")
        }
    }

    private fun setNewPlayerLives(remainingLives: Int){
        livesTextView.post{
            livesTextView.setText("${mainActivity.applicationContext.getString(R.string.livesText)}: ${remainingLives}")
            mainActivity.dataBaseHelper.updateGameData(score, lives, gameField.getEnemy(), wichLevel, 1)
        }
    }

}