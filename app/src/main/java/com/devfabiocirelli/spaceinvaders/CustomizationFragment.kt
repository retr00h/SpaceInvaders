package com.devfabiocirelli.spaceinvaders

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ListView
import kotlinx.android.synthetic.main.fragment_customization.*
import kotlinx.android.synthetic.main.fragment_customization.view.*

class CustomizationFragment(private val mainActivity: MainActivity) : Fragment() {

    private lateinit var backButton: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_customization, container, false)

        backButton = rootView.findViewById(R.id.imageButtonBackCustomization)
        backButton.setOnClickListener {
            if (mainActivity.settings.vibrations) mainActivity.vibe.vibrate(80)

            val fragmentManager = this.requireActivity().supportFragmentManager
            //Serve per tornare indietro alla pressione del tasto indietro
            fragmentManager.popBackStack()
        }

        //imposta la textView all'avvio con le personalizzazioni dell'utente salvate attualmente nel database
        val selectedCustom = mainActivity.dataBaseHelper.readCustomization()
        rootView?.textView?.text = (
                mainActivity.getString(R.string.currentSelectedCustomization) + "\n"
                        + mainActivity.getString(R.string.SpaceshipModel) + "${selectedCustom.ship}\n"
                        + mainActivity.getString(R.string.selectedColor) + "${selectedCustom.color}")

        //TODO: array di immagini delle navi
        val ship = arrayOf(
            R.mipmap.ic_playership_1_foreground,
            R.mipmap.ic_playership_2_foreground,
            R.mipmap.ic_playership_3_foreground,
            R.mipmap.ic_playership_4_foreground,
            R.mipmap.ic_playership_5_foreground,
            R.mipmap.ic_playership_6_foreground,
            R.mipmap.ic_playership_7_foreground,
            R.mipmap.ic_playership_8_foreground
        )

        //TODO: array di colori
        val colors = arrayOf(
                R.color.green,
                R.color.blue,
                R.color.red,
                R.color.grey,
                R.color.yellow,
                R.color.white
        )

        val list_view_ship = rootView.findViewById<ListView>(R.id.list_view_ship)
        val list_view_color = rootView.findViewById<ListView>(R.id.list_view_color)

        //Popola le listView con le immagini e i colori disponibili
        list_view_ship.adapter = MyAdapter(requireContext(), ship, R.layout.ship_model_view)
        list_view_color.adapter = MyAdapter(requireContext(), colors, R.layout.bullett_color_view)

        /*
        I due listener ottengono la posizione selezionata dall'utente e aggiornano nel database
        le personalizzazioni scelte
         */
        list_view_ship.setOnItemClickListener{ parent, view, position, id ->
            if (mainActivity.settings.vibrations) mainActivity.vibe.vibrate(80)

            val selectedCustom = mainActivity.dataBaseHelper.readCustomization()
            val element = parent.getItemAtPosition(position) as Int
            var color = selectedCustom.color
            mainActivity.dataBaseHelper.updateCustomization(element, color!!)
            setText()
        }

        list_view_color.setOnItemClickListener{ parent, view, position, id ->
            if (mainActivity.settings.vibrations) mainActivity.vibe.vibrate(80)

            val selectedCustom = mainActivity.dataBaseHelper.readCustomization()
            val element = parent.getItemAtPosition(position) as Int
            var ship = selectedCustom.ship
            mainActivity.dataBaseHelper.updateCustomization(ship!!, element)
            setText()
        }

        return rootView
    }

    //Imposta il testo della textView con le personalizzazioni selezionate dall'utente
    private fun setText() {
        val selectedCustom = mainActivity.dataBaseHelper.readCustomization()
            textView?.text = (
                mainActivity.getString(R.string.currentSelectedCustomization) + "\n"
                        + mainActivity.getString(R.string.SpaceshipModel) + "${selectedCustom.ship}\n"
                        + mainActivity.getString(R.string.selectedColor) + "${selectedCustom.color}")
    }


}