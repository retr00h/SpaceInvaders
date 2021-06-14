package com.devfabiocirelli.spaceinvaders

import kotlinx.android.synthetic.main.fragment_game.*
import kotlin.random.Random

class GameThread(val mainActivity: MainActivity, val gameFragment: GameFragment) : Thread() {

    override fun run() {
        super.run()

        var timing = 0
        val random = Random
        gameFragment.bloccaThread = false
        while (!gameFragment.bloccaThread) {
            //Se il giocatore ha sparato, entra nell'if e chiede alla view di ridisegnarsi ogni 100 millisecondi
            try {
                if(gameFragment.gameField.giocatoreColpito){
                    if (mainActivity.settings.vibrations) mainActivity.vibe.vibrate(80)

                    gameFragment.lives = gameFragment.gameField.playerLives
                    gameFragment.gameField.giocatoreColpito = false
                    gameFragment.setNewPlayerLives(gameFragment.lives)
                    if(gameFragment.gameField.playerLives <= 0){
                        mainActivity.gameOverFragment()
                        break
                    }
                }
                if (gameFragment.fire) {
                    var fine = gameFragment.gameField.onClickFire()
                    gameFragment.gameField.invalidate()
                    if (fine == 0) {
                        gameFragment.fire = false
                    }
                }
                timing++
                /*
                Il seguente if setta un cooldown in modo che il giocatore possa sparare un proiettile
                ogni 0,9 secondi (dopo che ha premuto il tasto per sparare, deve atendere 9 cicli del thread
                prima di poter sparare nuovamente)
                 */
                if(timing % gameFragment.fireCicle == 0 || timing >= 10){
                    gameFragment.alreadyClicked = false
                    timing = 0
                }
                /*
                la variabile timing serve per dare "velocita'" diverse agli elementi presenti sul canvas,
                timing % 2 == 0 ridisegna i nemici ogni due cicli (mentre i proiettili vengono ridisegnati ad ogni ciclo)
                 */
                if (timing % gameFragment.enemySpeed == 0) {
                    if (gameFragment.gameField.start) {
                        gameFragment.gameField.enemyUpdatePosition()
                    }

                }

                //quando il giocatore colpisce un nemico, aggiorna il punteggio tramite setNewScore()
                if(gameFragment.gameField.nemicoColpito) {
                    if (mainActivity.settings.vibrations) mainActivity.vibe.vibrate(80)

                    gameFragment.gameField.nemicoColpito = false
                    gameFragment.score += 50
                    gameFragment.setNewScore(gameFragment.score)
                }

                //se il gioco è partito, i nemici sparano
                if(gameFragment.gameField.start){
                    gameFragment.gameField.enemyFire(random.nextInt(0, gameFragment.gameField.numEnemy), mainActivity)
                }

                Thread.sleep(100)

                if(gameFragment.gameField.getEnemy() <= 0){
                    gameFragment.score += 50
                    gameFragment.setNewScore(gameFragment.score)
                    mainActivity.scoreFragment()
                    break
                }

            } catch (e :NullPointerException) {
                break
            }


        }
    }
}