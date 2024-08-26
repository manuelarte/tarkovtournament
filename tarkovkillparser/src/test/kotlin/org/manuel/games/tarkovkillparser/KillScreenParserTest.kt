package org.manuel.games.tarkovkillparser

import nu.pattern.OpenCV
import org.junit.jupiter.api.Assertions.assertEquals
import org.opencv.highgui.HighGui
import org.opencv.imgcodecs.Imgcodecs.IMREAD_GRAYSCALE
import kotlin.test.Ignore
import kotlin.test.Test

class KillScreenParserTest {

    @Test
    fun parseNoKillImage() {
        OpenCV.loadLocally()
        val img = "no-kills_en_TRAINING_1440x2560.png".toResourceMat(this::class.java.classLoader, IMREAD_GRAYSCALE)
        val killScreenParser = KillScreenParser(img = img, ocrService = TesseractService("C:\\Program Files\\Tesseract-OCR\\tessdata"))
        val actual = killScreenParser.parse()
        val expected = PlayerKillRaidInfo(listOf(), RaidMetadata("0.15.0.2.32197", Version.BETA, "TRAINING"))
        assertEquals(expected, actual)
    }

    @Test
    @Ignore("Test to show the image")
    fun cropNextBackImage() {
        OpenCV.loadLocally()
        val img = "no-kills_en_TRAINING_1440x2560.png".toResourceMat(this::class.java.classLoader, IMREAD_GRAYSCALE)
        val raidInfo = img.cropNextBack()
        HighGui.imshow("Next Back", raidInfo)
        HighGui.waitKey(0)
    }

    @Test
    @Ignore("Test to show the image")
    fun cropRaidInfoImage() {
        OpenCV.loadLocally()
        val img = "no-kills_en_TRAINING_1440x2560.png".toResourceMat(this::class.java.classLoader, IMREAD_GRAYSCALE)
        val raidInfo = img.cropRaidMetadata()
        HighGui.imshow("Raid Info", raidInfo)
        HighGui.waitKey(0)
    }
}
