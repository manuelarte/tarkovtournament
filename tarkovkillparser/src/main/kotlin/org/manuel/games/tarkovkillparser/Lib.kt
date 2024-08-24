@file:Suppress("ktlint:standard:filename")

package org.manuel.games.tarkovkillparser

import org.opencv.core.Mat

class KillParser(
    private val img: Mat,
    private val lang: String = "en",
) {
    init {
        require(!img.empty()) { "Img must not be empty!" }
    }

    fun parseRaidId(): String {
        // crop image to the bottom left
        // convert to black and white
        // invert colours
        // send to tesseract
        return "MANUEL"
    }
}
