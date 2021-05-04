package com.devfabiocirelli.spaceinvaders

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Switch
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.util.*

class SettingsFragment(private val mainActivity: MainActivity) : Fragment() {
    val TAG = "SettingsFragment"

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
            // Inflate the layout for this fragment
            super.onCreateView(inflater, container, savedInstanceState)

            /*
                questo blocco di codice e' necessario per instanziare il fragment, prelevare il
                button, e dargli un onClickListener che possa reagire ai clic su di esso
             */
            val rootView = inflater.inflate(R.layout.fragment_settings, container, false)
            val languageBtn = rootView.findViewById<Button>(R.id.languageButton)
            val backBtn = rootView.findViewById<ImageButton>(R.id.imageButtonBack)
            val soundSwitch = rootView.findViewById<Switch>(R.id.soundSwitch)
            val hapticSwitch = rootView.findViewById<Switch>(R.id.hapticSwitch)

            // inizializza gli switch con i valori in settings (che a questo punto dell'esecuzione
            // sono sicuramente non nulli)
            soundSwitch.isChecked = mainActivity.settings!!.audio!!
            hapticSwitch.isChecked = mainActivity.settings!!.vibrations!!

            // se soundSwitch viene cliccato, vengono creati nuovi Settings coi valori aggiornati
            // e vengono aggiornati anche i valori nel database remoto
            soundSwitch.setOnClickListener {
                Log.i(TAG, "Sound switch pressed")
                mainActivity.settings!!.audio = soundSwitch.isChecked
                mainActivity.userReference.child("settings").setValue(mainActivity.settings)
            }

            // se hapticSwitch viene cliccato, vengono creati nuovi Settings coi valori aggiornati
            // e vengono aggiornati anche i valori nel database remoto
            hapticSwitch.setOnClickListener {
                Log.i(TAG, "Haptic switch pressed")
                mainActivity.settings!!.vibrations = hapticSwitch.isChecked
                mainActivity.userReference.child("settings").setValue(mainActivity.settings)
            }

            // funzione lambda che aggiorna la locale a livello di activity al clic sul bottone,
            // e i valori in settings e nel database remoto
            languageBtn.setOnClickListener {
                Log.i(TAG, "Change language button pressed")

                val newLocale = when (languageBtn.text) {
                    // il testo del bottone cliccato è "Italiano", vuol dire che la lingua deve passare
                    // da italiano ad inglese
                    "Italiano" -> Locale.ENGLISH
                    // viceversa
                    "English" -> Locale.ITALIAN
                    else -> Locale.ENGLISH
                }

                // TODO: il testo del toast è sempre mostrato in Inglese
                Toast.makeText(mainActivity.applicationContext, R.string.language_will_be_applied, Toast.LENGTH_SHORT).show()

                // vengono creati nuovi Settings coi valori aggiornati
                // e vengono aggiornati anche i valori nel database remoto.
                // non c'è bisogno di aggiornare la lingua qui, in quanto viene aggiornata nel listener
                // su mainActivity.userReference.child("settings")
                mainActivity.settings!!.locale = newLocale.toString()
                mainActivity.userReference.child("settings").setValue(mainActivity.settings)
            }

            // funzione lambda che ritorna allo startFragment al clic sul bottone
            backBtn.setOnClickListener {
                Log.i(TAG, "Back button pressed")
                val fragmentManager = this.requireActivity().supportFragmentManager
                //Serve per tornare indietro alla pressione del tasto indietro
                fragmentManager.popBackStack()
            }

            return rootView
    }
}