package com.devfabiocirelli.spaceinvaders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_game.*

class GameFragment(val mainActivity: MainActivity) : Fragment() {

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

    lateinit var playerMovementThread: PlayerMovementThread
    lateinit var gameThread: GameThread

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_game, container, false)
        levelText = rootView.findViewById(R.id.levelIntView)
        livesTextView = rootView.findViewById(R.id.livesInt)
        scoreText = rootView.findViewById(R.id.scoreIntView)

        val gameData = mainActivity.dataBaseHelper.readGameData()

        wichLevel = gameData.level
        lives = gameData.lives
        score = gameData.score

        levelText.setText("${mainActivity.getString(R.string.LevelText)}: ${wichLevel}")
        livesTextView.setText("${mainActivity.getString(R.string.livesText)}: ${lives}")
        scoreText.setText("${mainActivity.getString(R.string.scoreText)}: ${score}")

        rootView.setOnTouchListener ( object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent): Boolean {

                // se l'utente preme o tiene premuto sullo schermo, a seconda di dove preme
                // (il 20% sinistro, il 20% destro, o il centro dello schermo) viene
                // cambiata una variabile nel playerMovementThread,
                // che quindi reagisce in modo diverso.
                // se invece l'utente rilascia lo schermo, il thread non fa nulla

                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        when {
                            event.x > rootView.width * 0.8 -> playerMovementThread.movement = 2
                            event.x <= rootView.width * 0.2 -> playerMovementThread.movement = 1
                            else -> playerMovementThread.movement = 0
                        }
                    }
                    MotionEvent.ACTION_UP -> {
                        playerMovementThread.movement = -1
                    }
                }

                return true
            }
        })

        gameThread = GameThread(mainActivity, this)

        return rootView
    }

    override fun onPause() {
        super.onPause()
        bloccaThread = true

        mainActivity.dataBaseHelper.updateGameData(score, lives, gameField.getEnemy(), wichLevel, 1)
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        bloccaThread = false

        gameThread = GameThread(mainActivity, this)
        gameThread.start()

        playerMovementThread = PlayerMovementThread(gameField, mainActivity, this)
    }


    fun setNewScore(newScore: Int){
        scoreText.post {
            scoreText.setText("${mainActivity.getString(R.string.scoreText)}: ${newScore}")
        }
    }

    fun setNewPlayerLives(remainingLives: Int){
        livesTextView.post{
            livesTextView.setText("${mainActivity.getString(R.string.livesText)}: ${remainingLives}")
            mainActivity.dataBaseHelper.updateGameData(score, lives, gameField.getEnemy(), wichLevel, 1)
        }
    }

}