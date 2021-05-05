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
    private val SIGN_IN_ACTIVITY_CODE = 1
    private var settingsValueListener: ValueEventListener? = null //getSettingsNodeValueListener()
    private var gameValueListener: ValueEventListener? = null // getGameNodeValueListener()
    private var startPageFragment = StartPageFragment(this)
    private var settingsFragment = SettingsFragment(this)
    private var uid: String? = null
    private var settings: Settings? = null
    private var gameData: GameData? = null
    private var canAddEventListeners = true
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var database: FirebaseDatabase
    private lateinit var userReference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.i(TAG, "onCreate")

        // inizializza la variabile backgroundAnimation, usata in onStart() e onStop()
        // per far partire/fermare l'animazione dello sfondo
        findViewById<FrameLayout>(R.id.contentFragment).apply {
            backgroundAnimation = background as AnimationDrawable
        }

        auth = Firebase.auth

//        // inizializza l'oggetto per l'autenticazione tramite account google
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken("39028420499-aiic36c5jquf3vhidk93a5rsi6imk7ut.apps.googleusercontent.com")
//                .requestEmail()
//                .build()
//        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // setta la variabile database e dà a reference la root dell'utente autenticato
        if (auth.currentUser != null) {
            database = FirebaseDatabase.getInstance()
            uid = auth.currentUser.uid
            userReference = database.reference.child(uid.toString())
            addEventListeners()
        } else {
            // inizializza l'oggetto per l'autenticazione tramite account google
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken("39028420499-aiic36c5jquf3vhidk93a5rsi6imk7ut.apps.googleusercontent.com")
                    .requestEmail()
                    .build()
            googleSignInClient = GoogleSignIn.getClient(this, gso)
        }

        // crea un'istanza di StartPageFragment, che viene inserita nel contentFragment e visualizzata
        val fragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.contentFragment, startPageFragment)
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

        if (auth.currentUser != null) addEventListeners()
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
        return object: ValueEventListener {
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
                    conf.locale = if (settings!!.locale!!.startsWith("en")) Locale.ENGLISH else Locale.ITALIAN
                    startPageFragment.applyAudio(settings!!.audio!!)
                }
                resources.updateConfiguration(conf, dm)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.i(TAG, "ERRORRRRR")
            }
        }
    }

    // funzione usata per inizializzare il game listener (che ascolta per cambiamenti nel nodo
    // uid/game del database remoto)
    private fun getGameNodeValueListener(): ValueEventListener {
        return object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                gameData = snapshot.getValue(GameData::class.java)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
    }

    // funzione pubblica che ritorna ture se l'utente è loggato, false altrimenti
    fun isUserLoggedIn() = uid != null

    // funzione pubblica che ritorna l'oggetto settings
    fun getSettings() = settings

    // funzione pubblica che ritorna l'oggetto gameData
    fun getGameData() = gameData

    // funzione pubblica che ritorna l'oggetto settingsFragment
    fun getSettingsFragment() = settingsFragment

    // funzione pubblica che ritorna l'oggetto startPageFragment
    fun getStartPageFragment() = startPageFragment

    // funzione pubblica che ritorna l'id della activity di login
    fun getSignInActivityCode() = SIGN_IN_ACTIVITY_CODE

    // funzione pubblica che ritorna l'oggetto auth (Firebase)
    fun getFirebaseAuth() = auth

    // funzione pubblica che ritorna l'oggetto googleSignInClient (per il login tramite Google)
    fun getGoogleSignInClient() = googleSignInClient

    // funzione pubblica che ritorna l'oggetto userReference (Firebase database)
    fun getUserReference() = userReference

    // funzione chiamata in StartPageFragment dopo il login (questa funzione esegue solo quando
    // l'utente si logga "manualmente" attraverso il bottone di login)
    fun login() {
        uid = auth.currentUser.uid
        database = FirebaseDatabase.getInstance()
        userReference = database.reference.child("$uid")
        addEventListeners()
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