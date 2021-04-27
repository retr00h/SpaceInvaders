package com.devfabiocirelli.spaceinvaders

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.FragmentTransaction

class StartPageFragment(private val mainActivity: MainActivity) : Fragment() {
    val TAG = "StartPageFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_start_page, container, false)
        val startBtn = rootView.findViewById<Button>(R.id.startButton)
        val optionsBtn = rootView.findViewById<ImageButton>(R.id.imageButtonOption)

        // funzione lambda che sposterà al fragment che mostra i salvataggi disponibili al clic sul bottone
        startBtn.setOnClickListener {
            Log.i(TAG, "Start Button Pressed")
            // TODO: come il codice sotto ma deve far partire il fragment della partita

            val fragment = GameFragment()
            val fragmentManager = this.requireActivity().supportFragmentManager
            val transaction: FragmentTransaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.contentFragment, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        // funzione lambda che sposta al settingsFragment (aggiungendolo alla backStack) al clic sul bottone
        optionsBtn.setOnClickListener {
            Log.i(TAG, "Options button pressed")

            val fragment = SettingsFragment(mainActivity)
            val fragmentManager = this.requireActivity().supportFragmentManager
            val transaction: FragmentTransaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.contentFragment, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        return rootView
    }
}