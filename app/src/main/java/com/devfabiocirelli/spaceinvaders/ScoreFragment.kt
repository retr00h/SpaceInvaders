package com.devfabiocirelli.spaceinvaders

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.fragment_customization.*
import kotlinx.android.synthetic.main.fragment_score.*

class ScoreFragment(val mainActivity: MainActivity) : Fragment() {
    lateinit var rootView: View
    lateinit var textView: TextView
    lateinit var nextLevelButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_score, container, false)
        textView = rootView.findViewById(R.id.textView2)
        nextLevelButton = rootView.findViewById(R.id.nextLevelButton)

        applyAudio(mainActivity.settings.audio)

        val gameData = mainActivity.dataBaseHelper.readGameData()
        var level = gameData.level
        var numEnemies = gameData.enemies
        var lives = gameData.lives
        var score = gameData.score

        level++

        if(level == 6){
            textView.setText("${mainActivity.getString(R.string.gameCompleted)}")
            nextLevelButton.setText("${mainActivity.getString(R.string.HomePage)}")
            nextLevelButton.setOnClickListener{
                mainActivity.dataBaseHelper.updateGameData(0, 3, 2, 1, 0)
                mainActivity.startPageFragment()
            }
        } else {
            textView.setText(
                "${mainActivity.getString(R.string.CompletedText)}\n" +
                        "score: ${score}\n" +
                        "next level: ${level}"
            )

            nextLevelButton.setOnClickListener {
                if (mainActivity.settings.vibrations) mainActivity.vibe.vibrate(80)

                numEnemies = level + 2
                mainActivity.dataBaseHelper.updateGameData(score, lives, numEnemies, level, 1)
                mainActivity.gameFragment()
            }
        }

        return rootView
    }

    fun applyAudio(audio: Boolean) {
        nextLevelButton.isSoundEffectsEnabled = audio
    }

}