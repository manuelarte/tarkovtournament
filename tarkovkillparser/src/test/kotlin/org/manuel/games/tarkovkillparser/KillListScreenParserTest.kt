package org.manuel.games.tarkovkillparser

import nu.pattern.OpenCV
import org.junit.jupiter.api.Assertions.assertEquals
import org.opencv.imgcodecs.Imgcodecs.IMREAD_GRAYSCALE
import kotlin.test.Test

class KillListScreenParserTest {
    // TODO it should returns no kills
    @Test
    fun parseNoKillImage() {
        OpenCV.loadLocally()
        val img = "./en/no-kills_en_TRAINING_1440x2560.png".toResourceMat(this::class.java.classLoader, IMREAD_GRAYSCALE)
        val killListScreenParser =
            KillListScreenParser(img = img, ocrService = TesseractService("C:\\Program Files\\Tesseract-OCR\\tessdata"))
        val actual = killListScreenParser.parse()
        val expected = PlayerKillRaidInfo(listOf(), RaidMetadata("0.15.0.2.32197", Version.BETA, "TRAINING"))
        assertEquals(expected, actual)
    }

    @Test
    fun parseManyKillImage() {
        OpenCV.loadLocally()
        val img = "./en/many-kills_en_FOXP5SB.png".toResourceMat(this::class.java.classLoader, IMREAD_GRAYSCALE)
        val killListScreenParser =
            KillListScreenParser(img = img, ocrService = TesseractService("C:\\Program Files\\Tesseract-OCR\\tessdata"))
        val actual = killListScreenParser.parse()
        val expected = PlayerKillRaidInfo(listOf(), RaidMetadata("0.15.0.0.32128", Version.BETA, "FOXP5SB"))
        assertEquals(expected, actual)
    }
}
