package org.manuel.games.tarkovkillparser

import nu.pattern.OpenCV
import org.opencv.core.Mat
import org.opencv.core.MatOfByte
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
        val bytes =
            this::class.java.classLoader
                .getResource("image-many-kills.png")!!
                .readBytes()
        val img =
            Imgcodecs.imdecode(MatOfByte(*bytes), IMREAD_UNCHANGED)
        val dest = Mat()
        Imgproc.cvtColor(img, dest, COLOR_BGR2GRAY)
        Imgcodecs.imwrite("resources/output.png", dest)
    }

    @Test
    fun isKillImageItIs() {
        OpenCV.loadLocally()
        val bytes =
            this::class.java.classLoader
                .getResource("image-many-kills-2.png")!!
                .readBytes()
        val img =
            Imgcodecs.imdecode(MatOfByte(*bytes), IMREAD_UNCHANGED)
        assertTrue { isKillImage(img) }
    }
}
