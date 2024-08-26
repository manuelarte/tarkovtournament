package org.manuel.games.tarkovkillparser.utils

import nu.pattern.OpenCV
import org.manuel.games.tarkovkillparser.toResourceMat
import org.opencv.core.Mat
import org.opencv.highgui.HighGui
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgcodecs.Imgcodecs.IMREAD_GRAYSCALE
import org.opencv.imgcodecs.Imgcodecs.IMREAD_UNCHANGED
import org.opencv.imgproc.Imgproc
import org.opencv.imgproc.Imgproc.COLOR_BGR2GRAY
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertTrue

class KillParserUtilsTest {
    @Test
    fun isKillImageImageWithKills() {
        OpenCV.loadLocally()
        val img = "./en/many-kills_en_FQXP5B.png".toResourceMat(this::class.java.classLoader, IMREAD_UNCHANGED)
        val dest = Mat().apply { Imgproc.cvtColor(img, this, COLOR_BGR2GRAY) }
        Imgcodecs.imwrite("resources/output.png", dest)
    }

    @Test
    fun isKillImageImageWithNoKills() {
        OpenCV.loadLocally()
        val img = "./en/no-kills_en_TRAINING_1440x2560.png".toResourceMat(this::class.java.classLoader, IMREAD_GRAYSCALE)
        val baseKillImage = Imgcodecs.imread("./src/main/resources/base-image-kill-parser.png")!!
        assertTrue { isKillImage(baseKillImage, img) }
    }

    @Test
    @Ignore("Test to show the image")
    fun cropNextBackImage() {
        OpenCV.loadLocally()
        val img = "en/no-kills_en_TRAINING_1440x2560.png".toResourceMat(this::class.java.classLoader, IMREAD_GRAYSCALE)
        val raidInfo = img.cropNextBack()
        HighGui.imshow("Next Back", raidInfo)
        HighGui.waitKey(0)
    }

    @Test
    @Ignore("Test to show the image")
    fun cropRaidInfoImage() {
        OpenCV.loadLocally()
        val img = "en/no-kills_en_TRAINING_1440x2560.png".toResourceMat(this::class.java.classLoader, IMREAD_GRAYSCALE)
        val raidInfo = img.cropRaidMetadata()
        HighGui.imshow("Raid Info", raidInfo)
        HighGui.waitKey(0)
    }

    @Test
    @Ignore("Test to show the image")
    fun cropPlayerKillInfoImage() {
        OpenCV.loadLocally()
        listOf("en/many-kills_en_FQXP5B.png", "en/no-kills_en_TRAINING_1440x2560.png", "es/some-kills_es_1440x2560.png").forEach {
            val img = it.toResourceMat(this::class.java.classLoader, IMREAD_GRAYSCALE)
            val playerKills = img.cropPlayerKillsTable()
            HighGui.imshow("Player Kills", playerKills)
            HighGui.waitKey(0)
            HighGui.destroyAllWindows()
        }
    }

    // @Ignore("Test to create the base image for no kills")
    @Test
    fun createBaseImageNoKillsFromTest() {
        OpenCV.loadLocally()
        val img = "en/no-kills_en_TRAINING_1440x2560.png".toResourceMat(this::class.java.classLoader, IMREAD_GRAYSCALE)
        val playerKills = img.cropPlayerKillsTable()
        HighGui.imshow("Player Kills", playerKills)
        HighGui.waitKey(0)
        HighGui.destroyAllWindows()
        Imgcodecs.imwrite("base-image-no-kills.png", playerKills)
    }
}
