package com.devfabiocirelli.spaceinvaders

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import android.widget.Toast


class StartPageFragment(private val mainActivity: MainActivity) : Fragment() {
    val TAG = "StartPageFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_start_page, container, false)
        val startBtn = rootView.findViewById<Button>(R.id.startButton)
        val optionsBtn = rootView.findViewById<ImageButton>(R.id.imageButtonOption)
        val resumeButton = rootView.findViewById<Button>(R.id.resumeButton)
        val newActivityButton = rootView.findViewById<Button>(R.id.newActivity)

        // funzione lambda che inizia una nuova partita
        // (chiedendo conferma in caso di dati già esistenti)
        startBtn.setOnClickListener {
            Log.i(TAG, "Start Button Pressed")

            // controlla se esiste già una partita salvata, e se c'è avvisa l'utente con un dialog
            if (mainActivity.gameData != null) {
                // visualizza il dialog
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle(getString(R.string.alertTitle))
                builder.setMessage(getString(R.string.alertMessage))
                // la prossima linea di codice fa sì che se l'utente tocca fuori dal dialog, questo
                // non viene annullato, ma persiste
                builder.setCancelable(false)
                builder.setPositiveButton(getString(R.string.alertPositiveButton)) { dialogInterface: DialogInterface, i: Int ->
                    startGame(mainActivity)
                }
                builder.setNegativeButton(getString(R.string.alertNegativeButton)) { dialogInterface: DialogInterface, i: Int ->}

                val alertDialog = builder.create()
                alertDialog.show()
            } else {
                startGame(mainActivity)
            }
        }

        resumeButton.setOnClickListener{
            // TODO: recuperare ultima partita dell'utente se disponibile
            if (mainActivity.gameData == null) {
                Toast.makeText(context, getString(R.string.noSavedGameAvailable), Toast.LENGTH_SHORT).show()
            } else {
                startGame(mainActivity)
            }
        }

        newActivityButton.setOnClickListener{
            startActivity(Intent(context, CustomizationActivity::class.java))
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

    private fun startGame(mainActivity: MainActivity) {
        val fragment = GameFragment(mainActivity)
        val fragmentManager = this.requireActivity().supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.contentFragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}