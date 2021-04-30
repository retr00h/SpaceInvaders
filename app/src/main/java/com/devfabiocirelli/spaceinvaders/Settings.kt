package com.devfabiocirelli.spaceinvaders

/*
    classe che rappresenta le impostazioni correnti
 */
data class Settings(val audio: Boolean?, val vibrations: Boolean?, val locale: String?) {
    override fun toString(): String {
        return "$audio, $vibrations, $locale"
    }

    constructor(): this(null, null, null)
}