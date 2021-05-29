package com.devfabiocirelli.spaceinvaders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_game.*
import kotlin.concurrent.thread

class GameFragment(val mainActivity: MainActivity, val gameData: GameData) : Fragment() {
    val TAG = "GameFragment"

    lateinit var rightButton: Button
    lateinit var leftButton: Button
    lateinit var fireButton: Button
    lateinit var rootView: View
    var fire = false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var rootView = inflater.inflate(R.layout.fragment_game, container, false)

        rootView = inflater.inflate(R.layout.fragment_game, container, false)
        rightButton = rootView.findViewById(R.id.rightButton)
        leftButton = rootView.findViewById(R.id.leftButton)
        fireButton = rootView.findViewById(R.id.fireButton)

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



        thread(start = true){
        while(true){
            //Se il giocatore ha sparato, entra nell'if e chiede alla view di ridisegnarsi ogni 10 millisecondi
            if(fire) {
                var fine = gameField.onClickFire()
                gameField.invalidate()
                if(fine == 0){
                    fire = false
                }
                }
            }
            if(gameField.start){
                gameField.enemyUpdatePosition()
            }
            Thread.sleep(10)

        }


        return rootView



    }



}