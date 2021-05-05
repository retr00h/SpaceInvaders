package com.devfabiocirelli.spaceinvaders

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase


class StartPageFragment(private val mainActivity: MainActivity) : Fragment() {
    val TAG = "StartPageFragment"
    lateinit var rootView: View
    lateinit var startBtn: Button
    lateinit var optionsBtn: ImageButton
    lateinit var resumeBtn: Button
    lateinit var newActivityButton: Button
    lateinit var signInButton: SignInButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_start_page, container, false)
        startBtn = rootView.findViewById(R.id.startButton)
        optionsBtn = rootView.findViewById(R.id.imageButtonOption)
        resumeBtn = rootView.findViewById(R.id.resumeButton)
        newActivityButton = rootView.findViewById(R.id.customizationActivity)

        signInButton = rootView.findViewById(R.id.signInButton)
        signInButton.setSize(SignInButton.SIZE_STANDARD)
        signInButton.setOnClickListener {
            val signInIntent: Intent = mainActivity.getGoogleSignInClient().signInIntent
            startActivityForResult(signInIntent, mainActivity.getSignInActivityCode())
        }

        if (!mainActivity.isUserLoggedIn()) {
            // audio e vibrazioni sono attive di default
            applyAudio(true)
        } else {
            // applica le impostazioni
            val settings = mainActivity.getSettings()
            if (settings != null) applyAudio(settings.audio!!)
        }

        if (mainActivity.isUserLoggedIn()) signInButton.isVisible = false

        // funzione lambda che inizia una nuova partita
        // (chiedendo conferma in caso di dati già esistenti)
        startBtn.setOnClickListener {
            Log.i(TAG, "Start Button Pressed")

            // controlla se esiste già una partita salvata, e se c'è avvisa l'utente con un dialog
            if (mainActivity.getSettings() != null) {
                if (mainActivity.getGameData() != null) {
                    // visualizza il dialog
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle(getString(R.string.alertTitle))
                    builder.setMessage(getString(R.string.alertMessage))
                    // la prossima linea di codice fa sì che se l'utente tocca fuori dal dialog, questo
                    // non viene annullato, ma persiste
                    builder.setCancelable(false)
                    builder.setPositiveButton(getString(R.string.alertPositiveButton)) { dialogInterface: DialogInterface, i: Int ->
                        startGame(mainActivity)
                    }
                    builder.setNegativeButton(getString(R.string.alertNegativeButton)) { dialogInterface: DialogInterface, i: Int ->}

                    val alertDialog = builder.create()
                    alertDialog.show()
                } else {
                    startGame(mainActivity)
                }
            } else {
                Toast.makeText(requireContext(), R.string.login_first, Toast.LENGTH_SHORT).show()
            }
        }

        resumeBtn.setOnClickListener{
            // TODO: recuperare ultima partita dell'utente se disponibile
            if (mainActivity.isUserLoggedIn()) {
                if (mainActivity.getGameData() == null) {
                    Toast.makeText(context, getString(R.string.noSavedGameAvailable), Toast.LENGTH_SHORT).show()
                } else {
                    startGame(mainActivity)
                }
            } else {
                Toast.makeText(requireContext(), R.string.login_first, Toast.LENGTH_SHORT).show()
            }
        }

        newActivityButton.setOnClickListener{
            val fragment = CustomizationFragment(mainActivity)
            val fragmentManager = this.requireActivity().supportFragmentManager
            val transaction: FragmentTransaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.contentFragment, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        // funzione lambda che sposta al settingsFragment (aggiungendolo alla backStack) al clic sul bottone
        optionsBtn.setOnClickListener {
            Log.i(TAG, "Options button pressed")

            val fragmentManager = mainActivity.supportFragmentManager
            val transaction: FragmentTransaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.contentFragment, mainActivity.getSettingsFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }
        return rootView
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == mainActivity.getSignInActivityCode()) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account =
                completedTask.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account?.idToken!!)
            // Signed in successfully, show authenticated UI.
            signInButton.isVisible = false
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        mainActivity.getFirebaseAuth().signInWithCredential(credential).addOnCompleteListener(mainActivity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    mainActivity.login()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                }
            }
    }

    private fun startGame(mainActivity: MainActivity) {
        val fragment = GameFragment(mainActivity, mainActivity.getGameData()!!)
        val fragmentManager = this.requireActivity().supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.contentFragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun applyAudio(audio: Boolean) {
        startBtn.isSoundEffectsEnabled = audio
        optionsBtn.isSoundEffectsEnabled = audio
        resumeBtn.isSoundEffectsEnabled = audio
        newActivityButton.isSoundEffectsEnabled = audio
        signInButton.isSoundEffectsEnabled = audio
    }
}