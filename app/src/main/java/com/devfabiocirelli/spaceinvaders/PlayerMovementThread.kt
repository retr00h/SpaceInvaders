package com.devfabiocirelli.spaceinvaders

class PlayerMovementThread(val gameField: Field, val mainActivity: MainActivity, val gameFragment: GameFragment) : Thread() {
    // movement = -1: not initialized
    // movement = 0: fire
    // movement = 1: left
    // movement = 2: right

    var movement = -1

    init {
        start()
    }

    override fun run() {
        super.run()
        while (true) {
            if (movement == 0) {
                if(!gameFragment.alreadyClicked) {
                    if (mainActivity.settings.vibrations) mainActivity.vibe.vibrate(80)
                    gameFragment.alreadyClicked = true
                    gameField.onClickAddBullet()
                    gameFragment.fire = true
                    gameField.onClickFire()
                }
            } else if (movement == 1) {
                gameField.onClickUpdateLeft()
            } else if (movement == 2) {
                gameField.onClickUpdateRight()
            }
            sleep(2L)
        }
    }
}