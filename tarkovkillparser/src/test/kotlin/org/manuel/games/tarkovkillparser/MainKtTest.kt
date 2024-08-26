package org.manuel.games.tarkovkillparser

import nu.pattern.OpenCV
import org.opencv.core.Mat
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgcodecs.Imgcodecs.IMREAD_UNCHANGED
import org.opencv.imgproc.Imgproc
import org.opencv.imgproc.Imgproc.COLOR_BGR2GRAY
import kotlin.test.Test
import kotlin.test.assertTrue

class MainKtTest {
    @Test
    fun testIsKillImage() {
        OpenCV.loadLocally()
        val img = "many-kills_en.png".toResourceMat(this::class.java.classLoader, IMREAD_UNCHANGED)
        val dest = Mat()
        Imgproc.cvtColor(img, dest, COLOR_BGR2GRAY)
        Imgcodecs.imwrite("resources/output.png", dest)
    }

    @Test
    fun isKillImageItIs() {
        OpenCV.loadLocally()
        val img = "many-kills-cropped_en.png".toResourceMat(this::class.java.classLoader, IMREAD_UNCHANGED)
        val baseKillImage = Imgcodecs.imread("./src/main/resources/base-image-kill-parser.png")!!
        assertTrue { isKillImage(baseKillImage, img) }
    }

    @Test
    fun isKillImageItNoKills() {
        OpenCV.loadLocally()
        val img = "no-kills_en_TRAINING_1440x2560.png".toResourceMat(this::class.java.classLoader, IMREAD_UNCHANGED)
        val baseKillImage = Imgcodecs.imread("./src/main/resources/base-image-kill-parser.png")!!
        assertTrue { isKillImage(baseKillImage, img) }
    }
}
