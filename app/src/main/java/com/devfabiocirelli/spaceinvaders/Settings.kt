package com.devfabiocirelli.spaceinvaders

/*
    classe che rappresenta le impostazioni correnti
 */
data class Settings(var audio: Boolean?, var vibrations: Boolean?, var locale: String?) {
    constructor(): this(null, null, null)
}