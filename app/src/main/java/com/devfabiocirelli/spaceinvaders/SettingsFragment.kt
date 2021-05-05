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
    lateinit var rootView: View
    lateinit var languageBtn: Button
    lateinit var backBtn: ImageButton
    lateinit var soundSwitch: Switch
    lateinit var hapticSwitch: Switch

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
            // Inflate the layout for this fragment
            super.onCreateView(inflater, container, savedInstanceState)

            /*
                questo blocco di codice e' necessario per instanziare il fragment, prelevare il
                button, e dargli un onClickListener che possa reagire ai clic su di esso
             */
            rootView = inflater.inflate(R.layout.fragment_settings, container, false)
            languageBtn = rootView.findViewById(R.id.languageButton)
            backBtn = rootView.findViewById(R.id.imageButtonBack)
            soundSwitch = rootView.findViewById(R.id.soundSwitch)
            hapticSwitch = rootView.findViewById(R.id.hapticSwitch)

            if (!mainActivity.isUserLoggedIn()) {
                // audio e vibrazioni sono attive di default
                applyAudio(true)
            } else {
                // applica le impostazioni
                val audio = mainActivity.getSettings()!!.audio!!
                applyAudio(audio)
            }

            // inizializza gli switch con i valori in settings (che a questo punto dell'esecuzione
            // sono sicuramente non nulli)
            soundSwitch.isChecked = if (!mainActivity.isUserLoggedIn()) true else mainActivity.getSettings()!!.audio!!
            hapticSwitch.isChecked = if (!mainActivity.isUserLoggedIn()) true else mainActivity.getSettings()!!.vibrations!!

            // se soundSwitch viene cliccato, vengono creati nuovi Settings coi valori aggiornati
            // e vengono aggiornati anche i valori nel database remoto
            soundSwitch.setOnClickListener {
                Log.i(TAG, "Sound switch pressed")
                mainActivity.getStartPageFragment().applyAudio(soundSwitch.isChecked)
                applyAudio(soundSwitch.isChecked)
                // se l'utente è loggato, salva anche nel db Firebase
                if (mainActivity.isUserLoggedIn()) {
                    mainActivity.getSettings()!!.audio = soundSwitch.isChecked
                    mainActivity.getUserReference().child("settings").setValue(mainActivity.getSettings())
                }
            }

            // se hapticSwitch viene cliccato, vengono creati nuovi Settings coi valori aggiornati
            // e vengono aggiornati anche i valori nel database remoto
            hapticSwitch.setOnClickListener {
                Log.i(TAG, "Haptic switch pressed")
                if (mainActivity.isUserLoggedIn()) {
                    mainActivity.getSettings()!!.vibrations = hapticSwitch.isChecked
                    mainActivity.getUserReference().child("settings").setValue(mainActivity.getSettings())
                }
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
                if (mainActivity.isUserLoggedIn()) {
                    mainActivity.getSettings()!!.locale = newLocale.toString()
                    mainActivity.getUserReference().child("settings").setValue(mainActivity.getSettings())
                } else {
                    val dm: DisplayMetrics = resources.displayMetrics
                    val conf = resources.configuration
                    conf.locale = newLocale
                    resources.updateConfiguration(conf, dm)
                }
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

    private fun applyAudio(audio: Boolean) {
        languageBtn.isSoundEffectsEnabled = audio
        backBtn.isSoundEffectsEnabled = audio
        soundSwitch.isSoundEffectsEnabled = audio
        hapticSwitch.isSoundEffectsEnabled = audio
    }
}