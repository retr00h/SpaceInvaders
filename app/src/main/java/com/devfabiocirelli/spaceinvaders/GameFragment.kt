package com.devfabiocirelli.spaceinvaders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class GameFragment(val mainActivity: MainActivity, val gameData: GameData) : Fragment() {
    val TAG = "GameFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_game, container, false)
        return rootView
    }
}