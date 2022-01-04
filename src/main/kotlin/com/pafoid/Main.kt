package com.pafoid

import com.pafoid.markii.Window
import com.pafoid.pafcraft.GameLoop

fun main(args:Array<String>){
    val gameLoop = GameLoop()
    val window = Window(
        title ="PafCraft",
        initCallback = { gameLoop.start() },
        drawCallback = { gameLoop.draw(it) },
        destroyCallback = { gameLoop.destroy() }
    )
    window.run()
}
