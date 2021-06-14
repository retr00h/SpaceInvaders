package com.devfabiocirelli.spaceinvaders

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Vibrator
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import java.util.*

/**
 * Questa classe è responsabile dell'avvio dell'applicazione:
 * - istanzia i fragment;
 * - gestisce l'animazione dello sfondo;
 * - contiene informazioni come impostazioni e dati di gioco, e li aggiorna a livello di
 *   database quando richiesto.
 */
class MainActivity : AppCompatActivity() {

    val startPageFragment = StartPageFragment(this)
    val settingsFragment = SettingsFragment(this)
    lateinit var gameFragment: GameFragment
    lateinit var gameOverFragment: gameOverFragment

    private lateinit var backgroundAnimation: AnimationDrawable
    lateinit var gameData: GameData
    lateinit var settings: Settings
    val dataBaseHelper = DataBaseHelper(this)

    lateinit var vibe: Vibrator

    /**
     * In questo metodo viene recuperata l'animazione dello sfondo
     * (usata poi in onStart() e onStop()), vengono letti impostazioni e dati di gioco dal db,
     * e posti in delle variabili, e viene applicata la localizzazione (lingua)
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // inizializza la variabile backgroundAnimation, usata in onStart() e onStop()
        // per far partire/fermare l'animazione dello sfondo
        findViewById<FrameLayout>(R.id.contentFragment).apply {
            backgroundAnimation = background as AnimationDrawable
        }

        // legge impostazioni e dati di gioco dal database, e applica la lingua
        settings = dataBaseHelper.readSettings()
        gameData = dataBaseHelper.readGameData()
        val dm: DisplayMetrics = resources.displayMetrics
        val conf = resources.configuration
        conf.locale = if (settings.locale.startsWith("en")) Locale.ENGLISH else Locale.ITALIAN
        resources.updateConfiguration(conf, dm)

        if(savedInstanceState == null){
            gameFragment = GameFragment(this)
        }

        vibe = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        // inserisce l'istanza di startPageFragment nel contentFragment e la visualizza
        val fragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.contentFragment, startPageFragment)
        transaction.commit()
    }

    override fun onStart() {
        super.onStart()
        // fa partire l'animazione del background
        backgroundAnimation.start()
    }

    override fun onStop() {
        super.onStop()
        // ferma l'animazione del background
        backgroundAnimation.stop()
    }

    /**
     * Quando la main activity ottiene il focus, questo metodo nasconde
     * la barra delle notifiche e la barra di navigazione
     */
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

    fun scoreFragment(){
        val nextLevelFragment = ScoreFragment(this)
        val fragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.contentFragment, nextLevelFragment)
        transaction.commit()
    }

    fun gameFragment(){
        gameFragment = GameFragment(this)
        val fragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.contentFragment, gameFragment)
        transaction.commit()
    }

    fun startPageFragment(){
        val fragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.contentFragment, startPageFragment)
        transaction.commit()
    }

    fun gameOverFragment(){
        gameOverFragment = gameOverFragment(this)
        val fragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.contentFragment, gameOverFragment)
        transaction.commit()
    }

    override fun onBackPressed() {

        val fragmentsList = supportFragmentManager.fragments
        if(fragmentsList.size > 0){
            val actualFragment = fragmentsList.get(fragmentsList.lastIndex)//get(fragmentsList.size - 1)
            if(actualFragment == gameFragment){
                supportFragmentManager.beginTransaction().remove(actualFragment).commit()
                supportFragmentManager.popBackStack()
            } else {
                super.onBackPressed()
            }

        }

    }

}