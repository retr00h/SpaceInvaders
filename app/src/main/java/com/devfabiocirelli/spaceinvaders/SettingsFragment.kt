package com.devfabiocirelli.spaceinvaders

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.util.*

class SettingsFragment : Fragment() {
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

            // funzione lambda che aggiorna la locale a livello di activity al clic sul bottone
            languageBtn.setOnClickListener {
                Log.i(TAG, "Change language button pressed")
                val activity = this.requireActivity()
                val language = languageBtn.text

                /*
                    il seguente codice modifica le resources attualmente in uso, nello specifico, crea
                    un nuovo locale (vedere il blocco if) e lo sostituisce a quello attualmente in uso
                 */
                val res = resources
                val dm: DisplayMetrics = res.getDisplayMetrics()
                val conf = res.getConfiguration()

                val newLocale = if (language == "Italiano") {
                    // il testo del bottone cliccato è "Italiano", vuol dire che la lingua deve passare
                    // da italiano ad inglese
                    Locale.ENGLISH
                } else {
                    // viceversa
                    Locale.ITALIAN
                }

                // TODO: il testo del toast è sempre mostrato in Inglese, fix asap
                Toast.makeText(activity.applicationContext, R.string.language_will_be_applied, Toast.LENGTH_SHORT).show()

                conf.locale = newLocale
                res.updateConfiguration(conf, dm)
            }

            // funzione lambda che ritorna allo startFragment al clic sul bottone
            backBtn.setOnClickListener {
                Log.i(TAG, "Back button pressed")
                val fragment = StartPageFragment()
                val fragmentManager = this.requireActivity().supportFragmentManager
                fragmentManager.popBackStack()
            }

            return rootView
    }
}