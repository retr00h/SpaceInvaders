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
            R.mipmap.ic_launcher, R.mipmap.ic_launcher_round, R.mipmap.ic_launcher_round, R.mipmap.ic_launcher, R.mipmap.ic_launcher_round, R.mipmap.ic_launcher_round
        )

        //TODO: array di colori
        val colors = arrayOf(
            R.color.green, R.color.blue, R.color.red, R.color.grey, R.color.white, R.color.darkGrey
        )
        //Popola le listView con le immagini e i colori disponibili
        list_view_ship.adapter = MyAdapter(this, ship, R.layout.ship_model_view)
        list_view_color.adapter = MyAdapter(this, colors, R.layout.ship_color_view)

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
                    customization = Customization(R.color.black, R.mipmap.ic_launcher)
                    mainActivity!!.userReference.child("customization").setValue(customization)
                    player.choseShip(R.mipmap.ic_launcher)

                } else {
                    //qui i parametri non possono essere null
                    player.choseShip(customization!!.ship!!)
                }
                resources.updateConfiguration(conf, dm)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i(TAG, "ERRORRRRR")
            }
        }
    }

}