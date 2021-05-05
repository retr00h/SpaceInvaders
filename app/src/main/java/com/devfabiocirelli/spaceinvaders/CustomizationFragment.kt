package com.devfabiocirelli.spaceinvaders

import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_customization.*

class CustomizationFragment(private val mainActivity: MainActivity) : Fragment() {

    private val referenceValueListener = getSettingsReferenceValueListener()
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
            R.color.green, R.color.blue, R.color.red, R.color.grey, R.color.white, R.color.darkGrey
        )

        val list_view_ship = rootView.findViewById<ListView>(R.id.list_view_ship)
        val list_view_color = rootView.findViewById<ListView>(R.id.list_view_color)

        //Popola le listView con le immagini e i colori disponibili
        list_view_ship.adapter = MyAdapter(requireContext(), ship, R.layout.ship_model_view)
        list_view_color.adapter = MyAdapter(requireContext(), colors, R.layout.ship_color_view)

        list_view_ship.setOnItemClickListener { parent, view, position, id ->
            val element = parent.getItemAtPosition(position)
            Log.i(TAG, "${element}-element")
            mainActivity.getUserReference().child("customization").setValue(element)
        }

        return rootView
    }

        override fun onStart() {
            super.onStart()
            mainActivity.getUserReference().child("customization")
                .addValueEventListener(referenceValueListener)
        }

        override fun onStop() {
            super.onStop()
            mainActivity.getUserReference().child("customization")
                .removeEventListener(referenceValueListener)
        }

        private fun getSettingsReferenceValueListener(): ValueEventListener {
            return object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    customization = snapshot.getValue(Customization::class.java)
                    val dm: DisplayMetrics = resources.displayMetrics
                    val conf = resources.configuration
                    if (customization == null) {
                        customization = Customization(R.color.black, R.drawable.ic_ship_1)
                        mainActivity.getUserReference().child("customization").setValue(customization)
                        //TODO: SETTARE LE PERSONALIZZAZIONI DI DEFAULT

                    } else {
                        //qui i parametri non possono essere null
                        //TODO: RECUPERARE LE PERSONALIZZAZIONI SCELTE
                    }
                    resources.updateConfiguration(conf, dm)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.i(TAG, "ERRORRRRR")
                }
            }
        }
}