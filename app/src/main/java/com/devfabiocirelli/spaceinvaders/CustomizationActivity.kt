package com.devfabiocirelli.spaceinvaders

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_customization.*
import kotlinx.android.synthetic.main.fragment_game.*
import java.util.*

class CustomizationActivity(private val mainActivity: MainActivity?) : AppCompatActivity() {

    private val referenceValueListener = getSettingsReferenceValueListener()
    private var customization: Customization? = null
    private val TAG = "CustomizationActivity"

    constructor(): this(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customization)

        //TODO: array delle immagini delle navi
        val ship = arrayOf(
            R.drawable.ic_ship_1, R.drawable.ic_ship_1, R.drawable.ic_ship_1, R.drawable.ic_ship_1, R.drawable.ic_ship_1
        )

        //TODO: array di colori
        val colors = arrayOf(
            R.color.green, R.color.blue, R.color.red, R.color.grey, R.color.white, R.color.darkGrey
        )
        //Popola le listView con le immagini e i colori disponibili
        list_view_ship.adapter = MyAdapter(this, ship, R.layout.ship_model_view)
        list_view_color.adapter = MyAdapter(this, colors, R.layout.ship_color_view)

        list_view_ship.setOnItemClickListener{parent, view, position, id ->
            val element = parent.getItemAtPosition(position) as Int
            mainActivity!!.userReference.child("customization").setValue(element)
        }

    }

    override fun onStart() {
       super.onStart()
       mainActivity?.userReference?.child("customization")?.addValueEventListener(referenceValueListener)
    }

    override fun onStop() {
       super.onStop()
        mainActivity?.userReference?.child("customization")?.removeEventListener(referenceValueListener)
   }

    private fun getSettingsReferenceValueListener(): ValueEventListener {
        return object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                customization = snapshot.getValue(Customization::class.java)
                val dm: DisplayMetrics = resources.displayMetrics
               val conf = resources.configuration
                if (customization == null) {
                    customization = Customization(R.color.black, R.drawable.ic_ship_1)
                    mainActivity!!.userReference.child("customization").setValue(customization)
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