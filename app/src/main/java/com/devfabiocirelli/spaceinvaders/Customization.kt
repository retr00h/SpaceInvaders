package com.devfabiocirelli.spaceinvaders

data class Customization(val color: Int?, val ship: Int?) {
    constructor(): this(null, null)
}