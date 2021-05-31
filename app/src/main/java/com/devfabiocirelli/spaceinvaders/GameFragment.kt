package com.devfabiocirelli.spaceinvaders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_game.*
import kotlin.concurrent.thread

class GameFragment(val mainActivity: MainActivity, var gameData: GameData) : Fragment() {
    val TAG = "GameFragment"

    lateinit var rightButton: Button
    lateinit var leftButton: Button
    lateinit var fireButton: Button
    lateinit var rootView: View
    lateinit var levelText: TextView
    lateinit var livesTextView: TextView
    lateinit var scoreText: TextView
    var fire = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        rootView = inflater.inflate(R.layout.fragment_game, container, false)
        rightButton = rootView.findViewById(R.id.rightButton)
        leftButton = rootView.findViewById(R.id.leftButton)
        fireButton = rootView.findViewById(R.id.fireButton)
        levelText = rootView.findViewById(R.id.levelIntView)
        livesTextView = rootView.findViewById(R.id.livesInt)
        scoreText = rootView.findViewById(R.id.scoreIntView)

        val wichLevel = gameData.level
        val numEnemies = gameData.enemies
        val lives = gameData.lives
        val score = gameData.score

        levelText.setText("${wichLevel}")
        livesTextView.setText("${lives}")
        scoreText.setText("${score}")
        //gameField.generateEnemy(numEnemies)

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
            while (true) {
                //Se il giocatore ha sparato, entra nell'if e chiede alla view di ridisegnarsi ogni 10 millisecondi
                    try {
                        if (fire) {
                            if (timing % 2 == 0) {
                                var fine = gameField.onClickFire()
                                gameField.invalidate()
                                if (fine == 0) {
                                    fire = false
                                }
                                timing = 0
                            }
                        }
                        timing++
                        if (gameField.start) {
                            gameField.enemyUpdatePosition()
                        }
                        Thread.sleep(100)
                    } catch (e :NullPointerException) {
                        break
                    }

            }
        }


        return rootView



    }

    override fun onPause() {
        super.onPause()
        val newGameData = mainActivity.dataBaseHelper.updateGameData(scoreText.text.toString().toInt(), livesTextView.text.toString().toInt(), gameField.getEnemy(), levelText.text.toString().toInt(), 1)

    }



}