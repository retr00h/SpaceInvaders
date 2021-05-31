package com.devfabiocirelli.spaceinvaders

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

val DATABASE_NAME = "SpaceInvaders.db"
val DATABASE_VERSION = 1

/**
 * Classe che gestisce le interazioni con il database SQLite.
 * Permette di leggere e aggiornare le informazioni relative alle impostazioni
 * e ai dati di gioco.
 */
class DataBaseHelper(var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    val SETTINGS_TABLE = "settings"
    val AUDIO_COL = "audio"
    val VIBRATIONS_COL = "vibrations"
    val LOCALE_COL = "locale"

    val GAME_TABLE = "game"
    val SCORE_COL = "score"
    val ENEMY_NUMBER_COL = "enemies"
    val LIVES_COL = "lives"
    val LEVEL_COL = "level"
    val RESUMABLEGAME_COL = "resumableGame"

    val CUSTOMIZATION_TABLE =  "customizations"
    val SHIP_COL = "spaceShips"
    val COLOR_COL = "spaceShipColors"

    /**
     * Questa funzione viene eseguita la prima volta che questa classe viene istanziata,
     * crea il database ed inizializza le tabelle.
     */
    override fun onCreate(db: SQLiteDatabase?) {
        val createSettingsTable = "CREATE TABLE $SETTINGS_TABLE ($AUDIO_COL INTEGER," +
                "$VIBRATIONS_COL INTEGER, $LOCALE_COL VARCHAR(2));"
        val createMatchTable = "CREATE TABLE $GAME_TABLE ($SCORE_COL INTEGER," +
                "$ENEMY_NUMBER_COL INTEGER, $LIVES_COL INTEGER, $LEVEL_COL INTEGER, $RESUMABLEGAME_COL INTEGER);"
        val createCustomizationsTable = "CREATE TABLE $CUSTOMIZATION_TABLE ($SHIP_COL INTEGER," + "$COLOR_COL INTEGER);"

        db?.execSQL(createSettingsTable)
        db?.execSQL(createMatchTable)
        db?.execSQL(createCustomizationsTable)
        val settingsValues = ContentValues()
        settingsValues.put(AUDIO_COL, 1)
        settingsValues.put(VIBRATIONS_COL, 1)
        settingsValues.put(LOCALE_COL, context.resources.configuration.locale.toString())
        db?.insert(SETTINGS_TABLE, null, settingsValues)

        val gameDataValues = ContentValues()
        gameDataValues.put(SCORE_COL, 0)
        gameDataValues.put(ENEMY_NUMBER_COL, 2)
        gameDataValues.put(LIVES_COL, 3)
        gameDataValues.put(LEVEL_COL, 1)
        gameDataValues.put(RESUMABLEGAME_COL, 0)
        db?.insert(GAME_TABLE, null, gameDataValues)

        val customizationsValues = ContentValues()
        customizationsValues.put(SHIP_COL, 1)
        customizationsValues.put(COLOR_COL, 1)
        db?.insert(CUSTOMIZATION_TABLE, null, customizationsValues)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
//        if (oldVersion < 2) db?.execSQL(DATABASE_ALTER_TABLE_1)
    }

    /**
     * Questa funzione permette di aggiornare i valori delle impostazioni,
     * aggiornando l'unica riga esistente.
     */
    fun updateSettings(audio: Boolean, vibrations: Boolean, locale: String) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(AUDIO_COL, audio)
        contentValues.put(VIBRATIONS_COL, vibrations)
        contentValues.put(LOCALE_COL, locale)

        val result = db.update(SETTINGS_TABLE, contentValues, null, null)
        // TODO: sostituire le stringhe delle toast con quelle di strings.xml
        if (result == 0) Toast.makeText(context, "Couldn't save new settings", Toast.LENGTH_SHORT).show()
        else {
            Toast.makeText(context, "New settings saved", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Questa funzione permette di leggere i valori delle impostazioni.
     */
    fun readSettings() : Settings {
        val audio: Boolean
        val vibrations: Boolean
        val locale: String

        val db = this.readableDatabase
        val query = "SELECT * FROM $SETTINGS_TABLE"
        val result = db.rawQuery(query, null)

        result.moveToFirst()
        audio = result.getInt(result.getColumnIndex(AUDIO_COL)) == 1
        vibrations = result.getInt(result.getColumnIndex(VIBRATIONS_COL)) == 1
        locale = result.getString(result.getColumnIndex(LOCALE_COL))

        result.close()
        return Settings(audio, vibrations, locale)
    }

    /**
     * Questa funzione permette di aggiornare i valori dei dati di gioco,
     * aggiornando l'unica riga esistente.
     */
    fun updateGameData(score: Int, lives: Int, enemies: Int, level: Int, resumableGame: Int) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(SCORE_COL, score)
        contentValues.put(LIVES_COL, lives)
        contentValues.put(ENEMY_NUMBER_COL, enemies)
        contentValues.put(LEVEL_COL, level)
        contentValues.put(RESUMABLEGAME_COL, resumableGame)

        val result = db.update(GAME_TABLE, contentValues, null, null)
        // TODO: sostituire le stringhe delle toast con quelle di strings.xml
        if (result == 0) Toast.makeText(context, "Couldn't save new settings", Toast.LENGTH_SHORT).show()
        else {
            Toast.makeText(context, "New settings saved", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Questa funzione permette di leggere i valori dei dati di gioco.
     */
    fun readGameData(): GameData {
        val score: Int
        val lives: Int
        val enemies: Int
        val level: Int
        val resumableGame: Int

        val db = this.readableDatabase
        val query = "SELECT * FROM $GAME_TABLE"
        val result = db.rawQuery(query, null)
        result.moveToFirst()
        score = result.getInt(result.getColumnIndex(SCORE_COL))
        lives = result.getInt(result.getColumnIndex(LIVES_COL))
        enemies = result.getInt(result.getColumnIndex(ENEMY_NUMBER_COL))
        level = result.getInt(result.getColumnIndex(LEVEL_COL))
        resumableGame = result.getInt(result.getColumnIndex(RESUMABLEGAME_COL))


        result.close()
        return GameData(score, lives, enemies, level, resumableGame)
    }

    fun updateCustomization(ship: Int, color: Int) {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(SHIP_COL, ship)
        contentValues.put(COLOR_COL, color)

        val result = db.update(CUSTOMIZATION_TABLE, contentValues, null, null)
        if (result == 0) Toast.makeText(context, context.getString(R.string.dbCustomUpdateError), Toast.LENGTH_SHORT).show()
        else {
            Toast.makeText(context, context.getString(R.string.dbCustomUpdateOk), Toast.LENGTH_SHORT).show()
        }
    }

    fun readCustomization(): Customization {

        val selectedSpaceShip: Int
        val selectedColor: Int

        val db = this.readableDatabase
        val query = "SELECT * FROM $CUSTOMIZATION_TABLE"
        val result = db.rawQuery(query, null)
        result.moveToFirst()
        selectedSpaceShip = result.getInt(result.getColumnIndex(SHIP_COL))
        selectedColor = result.getInt(result.getColumnIndex(COLOR_COL))
        result.close()
        return Customization(selectedColor, selectedSpaceShip)

    }
}