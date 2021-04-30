package com.devfabiocirelli.spaceinvaders

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.FragmentTransaction
import kotlinx.android.synthetic.main.fragment_start_page.*

class StartPageFragment : Fragment() {
    val TAG = "StartPageFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_start_page, container, false)
        val startBtn = rootView.findViewById<Button>(R.id.startButton)
        val optionsBtn = rootView.findViewById<ImageButton>(R.id.imageButtonOption)
        val newActivityButton = rootView.findViewById<Button>(R.id.newActivity)

        // funzione lambda che sposterà al fragment che mostra i salvataggi disponibili al clic sul bottone
        startBtn.setOnClickListener {
            Log.i(TAG, "Start Button Pressed")
            // TODO: come il codice sotto ma deve far partire il fragment della partita
        }

        /*resumeButton.setOnClickListener{
            // TODO: recuperare ultima partita dell'utente se disponibile
        }*/

        newActivityButton.setOnClickListener{
            startActivity(Intent(context, CustomizationActivity::class.java))
        }

        // funzione lambda che sposta al settingsFragment (aggiungendolo alla backStack) al clic sul bottone
        optionsBtn.setOnClickListener {
            Log.i(TAG, "Options button pressed")

            val fragment = SettingsFragment()
            val fragmentManager = this.requireActivity().supportFragmentManager
            val transaction: FragmentTransaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.contentFragment, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        return rootView
    }
}