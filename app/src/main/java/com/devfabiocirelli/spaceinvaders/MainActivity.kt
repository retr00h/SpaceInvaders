package com.devfabiocirelli.spaceinvaders

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import java.util.*


class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    private var referenceValueListener = getSettingsReferenceValueListener()
    private lateinit var auth: FirebaseAuth
    lateinit var database: FirebaseDatabase
    lateinit var reference: DatabaseReference
    var user: FirebaseUser? = null
    var settings: Settings? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i(TAG, "onCreate")

        // autentica in modo anonimo
        auth = Firebase.auth
        user = auth.currentUser
        auth.signInAnonymously()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInAnonymously:success")
                    user = auth.currentUser
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInAnonymously:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }

        // setta la variabile database e dà a reference la root dell'utente autenticato
        database = FirebaseDatabase.getInstance()
        reference = database.reference.child("${user?.uid}")

        // crea un'istanza di StartPageFragment, che viene inserita nel contentFragment e visualizzata
        val fragment = StartPageFragment(this)
        val fragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.contentFragment, fragment)
        transaction.commit()
    }

    override fun onStart() {
        super.onStart()
        reference.child("settings").addValueEventListener(referenceValueListener)
    }

    override fun onStop() {
        super.onStop()
        reference.child("settings").removeEventListener(referenceValueListener)
    }

    private fun getSettingsReferenceValueListener(): ValueEventListener {
        return object: ValueEventListener {
            // all'avvio, e quando un dato qualunque in userID/settings viene modificato,
            // aggiorna la variabile settings e la lingua a livello di app
            override fun onDataChange(snapshot: DataSnapshot) {
                settings = snapshot.getValue(Settings::class.java)
                val dm: DisplayMetrics = resources.displayMetrics
                val conf = resources.configuration
                if (settings == null) {
                    settings = Settings(true, true, "en_US")
                    reference.child("settings").setValue(settings)
                    conf.locale = Locale.ENGLISH
                } else {
                    conf.locale = if (settings!!.locale!!.startsWith("en")) Locale.ENGLISH else Locale.ITALIAN
                }
                resources.updateConfiguration(conf, dm)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i(TAG, "ERRORRRRR")
            }
        }
    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    private fun hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }
}