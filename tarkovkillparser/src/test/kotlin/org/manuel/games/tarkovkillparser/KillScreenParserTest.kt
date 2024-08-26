package org.manuel.games.tarkovkillparser

import nu.pattern.OpenCV
import org.junit.jupiter.api.Assertions.*
import org.opencv.imgcodecs.Imgcodecs.IMREAD_GRAYSCALE
import kotlin.test.Test

class KillScreenParserTest {
    @Test
    fun parseNoKillImage() {
        OpenCV.loadLocally()
        val img = "no-kills_en_1440x2560.png".toResourceMat(this::class.java.classLoader, IMREAD_GRAYSCALE)
        val killScreenParser = KillScreenParser(img = img, ocrService = TesseractService())
        killScreenParser.parse()
    }
}
