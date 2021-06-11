package com.devfabiocirelli.spaceinvaders

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlinx.android.synthetic.main.fragment_game.*

class gameOverFragment(val mainActivity: MainActivity) : Fragment() {

    lateinit var rootView: View
    lateinit var restartButton: Button
    lateinit var homeButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.fragment_game_over, container, false)
        restartButton = rootView.findViewById(R.id.restartButton)
        homeButton = rootView.findViewById(R.id.homeButton)

        restartButton.setOnClickListener{
            mainActivity.dataBaseHelper.updateGameData(0, 3, 2, 1, 1)
            mainActivity.gameFragment()
        }

        homeButton.setOnClickListener{
            mainActivity.startPageFragment()
        }

        return rootView
    }

}