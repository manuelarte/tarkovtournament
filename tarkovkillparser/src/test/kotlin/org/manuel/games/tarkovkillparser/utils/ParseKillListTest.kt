package org.manuel.games.tarkovkillparser.utils

import nu.pattern.OpenCV
import org.junit.jupiter.api.Test
import org.manuel.games.tarkovkillparser.toResourceMat
import org.opencv.highgui.HighGui
import org.opencv.imgcodecs.Imgcodecs.IMREAD_GRAYSCALE
import kotlin.test.Ignore

class ParseKillListTest {
    @Test
    @Ignore
    fun parseKillEntryExample() {
        OpenCV.loadLocally()
        val img = "en/many-kills_en_FQXP5B.png".toResourceMat(this::class.java.classLoader, IMREAD_GRAYSCALE)
        val parser = ParseKillList(img.cropPlayerKillsTable())
        for (n in 1..9) {
            val killEntry = parser.imgKillEntry(n)
            HighGui.imshow("Kill Entry $n", killEntry)
            HighGui.waitKey(0)
            HighGui.destroyAllWindows()
        }
    }

    @Test
    @Ignore
    fun parseKillEntryLocationExample() {
        OpenCV.loadLocally()
        val img = "en/many-kills_en_FQXP5B.png".toResourceMat(this::class.java.classLoader, IMREAD_GRAYSCALE)
        val parser = ParseKillList(img.cropPlayerKillsTable())
        for (n in 1..9) {
            val croppedImage = parser.imgKillEntry(n).let { parser.imgKillEntryLocation(it) }
            HighGui.imshow("Location $n", croppedImage)
            HighGui.waitKey(0)
            HighGui.destroyAllWindows()
        }
    }

    @Test
    @Ignore
    fun parseKillEntryTimeExample() {
        OpenCV.loadLocally()
        val img = "en/many-kills_en_FQXP5B.png".toResourceMat(this::class.java.classLoader, IMREAD_GRAYSCALE)
        val parser = ParseKillList(img.cropPlayerKillsTable())
        for (n in 1..9) {
            val croppedImage = parser.imgKillEntry(n).let { parser.imgKillEntryTime(it) }
            HighGui.imshow("Time $n", croppedImage)
            HighGui.waitKey(0)
            HighGui.destroyAllWindows()
        }
    }

    @Test
    @Ignore
    fun parseKillEntryPlayerExample() {
        OpenCV.loadLocally()
        val img = "en/many-kills_en_FQXP5B.png".toResourceMat(this::class.java.classLoader, IMREAD_GRAYSCALE)
        val parser = ParseKillList(img.cropPlayerKillsTable())
        for (n in 1..9) {
            val croppedImage = parser.imgKillEntry(n).let { parser.imgKillEntryPlayer(it) }
            HighGui.imshow("Player $n", croppedImage)
            HighGui.waitKey(0)
            HighGui.destroyAllWindows()
        }
    }

    @Test
    @Ignore
    fun parseKillEntryLevelExample() {
        OpenCV.loadLocally()
        val img = "en/many-kills_en_FQXP5B.png".toResourceMat(this::class.java.classLoader, IMREAD_GRAYSCALE)
        val parser = ParseKillList(img.cropPlayerKillsTable())
        for (n in 1..9) {
            val croppedImage = parser.imgKillEntry(n).let { parser.imgKillEntryLevel(it) }
            HighGui.imshow("Level $n", croppedImage)
            HighGui.waitKey(0)
            HighGui.destroyAllWindows()
        }
    }

    @Test
    @Ignore
    fun parseKillEntryFactionExample() {
        OpenCV.loadLocally()
        val img = "en/many-kills_en_FQXP5B.png".toResourceMat(this::class.java.classLoader, IMREAD_GRAYSCALE)
        val parser = ParseKillList(img.cropPlayerKillsTable())
        for (n in 1..9) {
            val croppedImage = parser.imgKillEntry(n).let { parser.imgKillEntryFaction(it) }
            HighGui.imshow("Level $n", croppedImage)
            HighGui.waitKey(0)
            HighGui.destroyAllWindows()
        }
    }

    @Test
    fun parseKillEntryStatusExample() {
        OpenCV.loadLocally()
        val img = "en/many-kills_en_FQXP5B.png".toResourceMat(this::class.java.classLoader, IMREAD_GRAYSCALE)
        val parser = ParseKillList(img.cropPlayerKillsTable())
        for (n in 1..9) {
            val croppedImage = parser.imgKillEntry(n).let { parser.imgKillEntryStatus(it) }
            HighGui.imshow("Level $n", croppedImage)
            HighGui.waitKey(0)
            HighGui.destroyAllWindows()
        }
    }
}
