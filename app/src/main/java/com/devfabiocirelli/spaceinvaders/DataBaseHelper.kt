package com.devfabiocirelli.spaceinvaders

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast

val DATABASE_NAME = "SpaceInvaders.db"
val DATABASE_VERSION = 1

class DataBaseHelper(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    val TAG = "DataBaseHelper"

    val SETTINGS_TABLE = "settings"
    val AUDIO_COL = "audio"
    val VIBRATIONS_COL = "vibrations"
    val LOCALE_COL = "locale"

    val MATCH_TABLE_NAME = "matches"
    val ID_COL = "id"
    val SCORE_COL = "score"
    val ENEMY_NUMBER_COL = "enemies"
    val LIVES_COL = "lives"
    val POWERUPS_COL = "powerups"

    // questa funzione viene eseguita alla creazione del db per la prima volta:
    // crea le (per ora) due tabelle ed inizializza quella delle impostazioni con dei valori
    // di default
    override fun onCreate(db: SQLiteDatabase?) {
        Log.i(TAG, "onCreate")
        val createSettingsTable = "CREATE TABLE $SETTINGS_TABLE ($AUDIO_COL INTEGER, $VIBRATIONS_COL INTEGER, $LOCALE_COL VARCHAR(2));"
        val createMatchTable = "CREATE TABLE $MATCH_TABLE_NAME ($ID_COL INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$SCORE_COL INTEGER, $ENEMY_NUMBER_COL INTEGER, $LIVES_COL INTEGER, $POWERUPS_COL INTEGER);"

        db?.execSQL(createSettingsTable)
        db?.execSQL(createMatchTable)
        val settingsValues = ContentValues()
        settingsValues.put(AUDIO_COL, 1)
        settingsValues.put(VIBRATIONS_COL, 1)
        settingsValues.put(LOCALE_COL, "en_US")
        val result = db?.insert(SETTINGS_TABLE, null, settingsValues)
        if (result == 0L) Log.i(TAG, "Couldn't initialize settings DB")
        else Log.i(TAG, "Settings DB initialized")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
//        if (oldVersion < 2) db?.execSQL(DATABASE_ALTER_TABLE_1)
    }

    // questa funzione permette di aggiornare i valori delle impostazioni, aggiornando l'unica riga
    // esistente
    fun updateSettings(audio: Boolean, vibrations: Boolean, locale: String) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(AUDIO_COL, if (audio) 1 else 0)
        contentValues.put(VIBRATIONS_COL, if (vibrations) 1 else 0)
        contentValues.put(LOCALE_COL, locale)
        val result = db.update(SETTINGS_TABLE, contentValues, null, null)
        // TODO: sostituire le stringhe delle toast con quelle di strings.xml
        if (result == 0) Toast.makeText(context, "Couldn't save new settings", Toast.LENGTH_SHORT).show()
        else {
            Toast.makeText(context, "New settings saved", Toast.LENGTH_SHORT).show()
            Log.i(TAG, "New settings saved")
        }
    }

    // questa funzione permette di leggere i valori delle impostazioni
    fun readSettings() : Triple<Boolean, Boolean, String> {
        val audio: Boolean
        val vibrations: Boolean
        val locale: String

        val db = this.readableDatabase
        val query = "SELECT * FROM $SETTINGS_TABLE"
        val result = db.rawQuery(query, null)

        if (result.moveToFirst()) {
            Log.i(TAG, "Successfully read settings")
            audio = result.getInt(result.getColumnIndex(AUDIO_COL)) == 1
            vibrations = result.getInt(result.getColumnIndex(VIBRATIONS_COL)) == 1
            locale = result.getString(result.getColumnIndex(LOCALE_COL))
        } else {
            Log.i(TAG, "Couldn't read settings")
            audio = true
            vibrations = true
            locale = "en_US"
        }
        result.close()
        return Triple(audio, vibrations, locale)
    }
}