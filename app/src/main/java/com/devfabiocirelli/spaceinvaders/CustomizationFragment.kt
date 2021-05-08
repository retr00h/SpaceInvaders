package com.devfabiocirelli.spaceinvaders

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import kotlinx.android.synthetic.main.fragment_customization.*

class CustomizationFragment(private val mainActivity: MainActivity) : Fragment() {

    private var customization: Customization? = null
    private val TAG = "CustomizationActivity"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_customization, container, false)

        // Inflate the layout for this fragment
        val ship = arrayOf(
            R.drawable.ic_ship_1,
            R.drawable.ic_ship_1,
            R.drawable.ic_ship_1,
            R.drawable.ic_ship_1,
            R.drawable.ic_ship_1
        )

        //TODO: array di colori
        val colors = arrayOf(
                R.color.green,
                R.color.blue,
                R.color.red,
                R.color.grey,
                R.color.white,
                R.color.darkGrey
        )

        val list_view_ship = rootView.findViewById<ListView>(R.id.list_view_ship)
        val list_view_color = rootView.findViewById<ListView>(R.id.list_view_color)

        //Popola le listView con le immagini e i colori disponibili
        list_view_ship.adapter = MyAdapter(requireContext(), ship, R.layout.ship_model_view)
        list_view_color.adapter = MyAdapter(requireContext(), colors, R.layout.ship_color_view)

        list_view_ship.setOnItemClickListener{ parent, view, position, id ->
            val element = parent.getItemAtPosition(position) as Int
            val selectedCustom = mainActivity.dataBaseHelper.readCustomization()
            val color = selectedCustom.color
            mainActivity.dataBaseHelper.updateCustomization(element, color!!)
        }

        list_view_color.setOnItemClickListener{ parent, view, position, id ->
            val element = parent.getItemAtPosition(position) as Int
            val selectedCustom = mainActivity.dataBaseHelper.readCustomization()
            val ship = selectedCustom.ship
            mainActivity.dataBaseHelper.updateCustomization(ship!!, element)
        }

        //Imposta il testo della textView con le personalizzazioni selezionate dall'utente
        val selectedCustom = mainActivity.dataBaseHelper.readCustomization()
        textView.text = (
            mainActivity.applicationContext.getString(R.string.currentSelectedCustomization) + "\n"
            + mainActivity.applicationContext.getString(R.string.SpaceshipModel) + "${selectedCustom.ship}\n"
            + mainActivity.applicationContext.getString(R.string.selectedColor) +"${selectedCustom.color}")

        return rootView
    }


}