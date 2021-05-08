package com.devfabiocirelli.spaceinvaders

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

/**
 * Questa classe rappresenta la prima schermata visibile al lancio dell'applicazione.
 * Permette di far partire una nuova partita, di riprenderne una già iniziata,
 * di andare alla schermata di personalizzazione della nave del giocatore, e permette di
 * accedere alla schermata delle impostazioni.
 */
class StartPageFragment(private val mainActivity: MainActivity) : Fragment() {
    lateinit var rootView: View
    lateinit var startBtn: Button
    lateinit var optionsBtn: ImageButton
    lateinit var resumeBtn: Button
    lateinit var newActivityButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_start_page, container, false)
        startBtn = rootView.findViewById(R.id.startButton)
        optionsBtn = rootView.findViewById(R.id.imageButtonOption)
        resumeBtn = rootView.findViewById(R.id.resumeButton)
        newActivityButton = rootView.findViewById(R.id.customizationActivity)

        applyAudio(mainActivity.settings.audio)

        // funzione lambda che inizia una nuova partita
        // (chiedendo conferma in caso di dati già esistenti)
        startBtn.setOnClickListener {
            // se uno qualunque dei dati di gioco equivale a -1, non c'è una partita salvata,
            // quindi se ne può iniziare direttamente una nuova.
            // se invece ci sono dati salvati, viene richiesta una conferma
            if (mainActivity.gameData.enemies == -1) {
                startGame()
            } else {
                // visualizza il dialog
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle(getString(R.string.alertTitle))
                builder.setMessage(getString(R.string.alertMessage))
                // la prossima linea di codice fa sì che se l'utente tocca fuori dal dialog, questo
                // non viene annullato, ma persiste
                builder.setCancelable(false)
                builder.setPositiveButton(getString(R.string.alertPositiveButton)) { _: DialogInterface, _: Int ->
                    startGame()
                }
                builder.setNegativeButton(getString(R.string.alertNegativeButton)) { _: DialogInterface, _: Int ->}

                val alertDialog = builder.create()
                alertDialog.show()
            }
        }

        // funzione lambda che permette di riprendere una partita se esiste
        resumeBtn.setOnClickListener{
            // se uno qualunque dei dati di gioco equivale a -1, non c'è una partita salvata,
            // quindi non si può riprendere una partita salvata
            if (mainActivity.gameData.enemies == -1) {
                Toast.makeText(context, getString(R.string.noSavedGameAvailable), Toast.LENGTH_SHORT).show()
            } else {
                startGame()
            }
        }

        // funzione lambda che visualizza il fragment di personalizzazione della nave del
        // giocatore
        newActivityButton.setOnClickListener{
//            val fragment = CustomizationFragment(mainActivity)
//            val fragmentManager = this.requireActivity().supportFragmentManager
//            val transaction: FragmentTransaction = fragmentManager.beginTransaction()
//            transaction.replace(R.id.contentFragment, fragment)
//            transaction.addToBackStack(null)
//            transaction.commit()
        }

        // funzione lambda che visualizza il fragment delle impostazioni dell'app
        optionsBtn.setOnClickListener {
            val fragmentManager = mainActivity.supportFragmentManager
            val transaction: FragmentTransaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.contentFragment, mainActivity.settingsFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        return rootView
    }

    // funzione visualizza il fragment di gioco
    private fun startGame() {
        val fragmentManager = this.requireActivity().supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.contentFragment, mainActivity.gameFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    // funzione che applica le impostazioni audio dei bottoni
    fun applyAudio(audio: Boolean) {
        startBtn.isSoundEffectsEnabled = audio
        optionsBtn.isSoundEffectsEnabled = audio
        resumeBtn.isSoundEffectsEnabled = audio
        newActivityButton.isSoundEffectsEnabled = audio
    }
}