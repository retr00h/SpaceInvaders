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
    val TAG = "GameFragment"

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
        numEnemies = gameData.enemies
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
            gameField.onClickAddBullet()
            fire = true
            gameField.onClickFire()

        }

        thread(start = true) {
            var timing = 0
            val random = Random
            while (true) {
                //Se il giocatore ha sparato, entra nell'if e chiede alla view di ridisegnarsi ogni 100 millisecondi
                    try {
                        if(bloccaThread){
                            break
                        }
                        if (fire) {
                            //if (timing % 2 == 0) {
                                var fine = gameField.onClickFire()
                                gameField.invalidate()
                                if (fine == 0) {
                                    fire = false
                                }
                            //timing = 0
                            //}
                        }
                        timing++
                        /*
                        la variabile timing serve per dare "velocita'" diverse agli elementi presenti sul canvas,
                        timing % 2 == 0 ridisegna i nemici ogni due cicli (mentre i proiettili vengono ridisegnati ad ogni ciclo)
                         */
                        if (timing % 2 == 0) {
                            if (gameField.start) {
                                gameField.enemyUpdatePosition()
                            }
                            timing = 0
                        }

                        if(gameField.colpito) {
                            gameField.colpito = false
                            score += gameField.points
                            setNewScore(score)
                        }

                        if(gameField.start){
                            gameField.enemyFire(random.nextInt(0, gameField.numEnemy))
                        }

                        Thread.sleep(100)

                        if(gameField.getEnemy() <= 0){
                            score += gameField.points
                            setNewScore(score)
                            mainActivity.scoreFragment()
                            setEnemies(numEnemies)
                            break
                        }

                    } catch (e :NullPointerException) {
                        break
                    }


            }
        }


        return rootView



    }

    override fun onStart() {
        super.onStart()
        setEnemies(numEnemies)
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

    private fun setEnemies(enemies: Int){
        gameField.generateEnemy(enemies)
    }

    private fun setNewScore(newScore: Int){
        scoreText.post {
            scoreText.setText("${mainActivity.applicationContext.getString(R.string.scoreText)}: ${newScore}")
        }
    }

}