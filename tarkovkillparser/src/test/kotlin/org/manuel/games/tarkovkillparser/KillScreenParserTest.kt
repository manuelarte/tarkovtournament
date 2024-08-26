package org.manuel.games.tarkovkillparser

import nu.pattern.OpenCV
import org.junit.jupiter.api.Assertions.assertEquals
import org.opencv.imgcodecs.Imgcodecs.IMREAD_GRAYSCALE
import kotlin.test.Test

class KillScreenParserTest {
    @Test
    fun parseNoKillImage() {
        OpenCV.loadLocally()
        val img = "./en/no-kills_en_TRAINING_1440x2560.png".toResourceMat(this::class.java.classLoader, IMREAD_GRAYSCALE)
        val killScreenParser = KillScreenParser(img = img, ocrService = TesseractService("C:\\Program Files\\Tesseract-OCR\\tessdata"))
        val actual = killScreenParser.parse()
        val expected = PlayerKillRaidInfo(listOf(), RaidMetadata("0.15.0.2.32197", Version.BETA, "TRAINING"))
        assertEquals(expected, actual)
    }
}
