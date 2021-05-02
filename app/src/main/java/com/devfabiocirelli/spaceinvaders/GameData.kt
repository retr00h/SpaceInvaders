package com.devfabiocirelli.spaceinvaders

data class GameData (var score: Long?, var lives: Float?, var enemies: Int?) {

    constructor(): this(null, null, null)
}