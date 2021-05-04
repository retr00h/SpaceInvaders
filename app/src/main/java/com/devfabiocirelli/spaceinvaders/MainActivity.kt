package com.devfabiocirelli.spaceinvaders

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import java.util.*


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var backgroundAnimation: AnimationDrawable
    val SIGN_IN_ACTIVITY_CODE = 1
    var settingsValueListener: ValueEventListener? = null //getSettingsNodeValueListener()
    var gameValueListener: ValueEventListener? = null // getGameNodeValueListener()
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var database: FirebaseDatabase
    lateinit var userReference: DatabaseReference
    lateinit var auth: FirebaseAuth
    lateinit var uid: String
    var settings: Settings? = null
    var gameData: GameData? = null
    var canAddEventListeners = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i(TAG, "onCreate")

        // inizializza la variabile backgroundAnimation, usata in onStart() e onStop()
        // per far partire/fermare l'animazione dello sfondo
        findViewById<FrameLayout>(R.id.contentFragment).apply {
            setBackgroundResource(R.drawable.background_animation)
            backgroundAnimation = background as AnimationDrawable
        }

        // inizializza l'oggetto per l'autenticazione tramite account google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("39028420499-aiic36c5jquf3vhidk93a5rsi6imk7ut.apps.googleusercontent.com")
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = Firebase.auth

        // questo metodo è chiamato ANCHE qui per evitare un crash provocato da un accesso alla
        // schermata impostazioni troppo prima che le impostazioni siano state lette dal database
//        addEventListeners()

        // crea un'istanza di StartPageFragment, che viene inserita nel contentFragment e visualizzata
        val fragment = StartPageFragment(this)
        val fragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.contentFragment, fragment)
        transaction.commit()
    }

    fun addEventListeners() {
        if (canAddEventListeners) {
            canAddEventListeners = false
            settingsValueListener = getSettingsNodeValueListener()
            gameValueListener = getGameNodeValueListener()
            userReference.child("settings").addValueEventListener(settingsValueListener!!)
            userReference.child("game").addValueEventListener(gameValueListener!!)
        }
    }

    private fun removeEventListeners() {
        if (!canAddEventListeners) {
            canAddEventListeners = true
            userReference.child("settings").removeEventListener(settingsValueListener!!)
            userReference.child("game").removeEventListener(gameValueListener!!)
        }
    }

    override fun onStart() {
        super.onStart()
        // fa partire l'animazione del background
        backgroundAnimation.start()

        // setta la variabile database e dà a reference la root dell'utente autenticato
        if (auth.currentUser != null) {
            database = FirebaseDatabase.getInstance()
            uid = auth.currentUser.uid
            userReference = database.reference.child(uid)
            addEventListeners()
        }
    }

    override fun onStop() {
        super.onStop()
        // ferma l'animazione del background
        backgroundAnimation.stop()

        if (auth.currentUser != null) removeEventListeners()
    }

    // funzione usata per inizializzare il settings listener (che ascolta per cambiamenti nel nodo
    // uid/settings del database remoto)
    private fun getSettingsNodeValueListener(): ValueEventListener {
        return object : ValueEventListener {
            // all'avvio, e quando un dato qualunque in userID/settings viene modificato,
            // aggiorna la variabile settings e la lingua a livello di app
            override fun onDataChange(snapshot: DataSnapshot) {
                settings = snapshot.getValue(Settings::class.java)
                val dm: DisplayMetrics = resources.displayMetrics
                val conf = resources.configuration
                if (settings == null) {
                    settings = Settings(true, true, conf.locale.toString())
                    userReference.child("settings").setValue(settings)
                    conf.locale = Locale.ENGLISH
                } else {
                    conf.locale =
                        if (settings!!.locale!!.startsWith("en")) Locale.ENGLISH else Locale.ITALIAN
                }

            }

                override fun onCancelled(error: DatabaseError) {
                    Log.i(TAG, "ERRORRRRR")
                }
            }
        }

        // funzione usata per inizializzare il game listener (che ascolta per cambiamenti nel nodo
        // uid/game del database remoto)
        private fun getGameNodeValueListener(): ValueEventListener {
            return object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    gameData = snapshot.getValue(GameData::class.java)
                }

                override fun onCancelled(error: DatabaseError) {

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