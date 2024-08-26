package org.manuel.games.tarkovkillparser

import nu.pattern.OpenCV
import org.opencv.imgcodecs.Imgcodecs.IMREAD_GRAYSCALE
import kotlin.test.Test
import kotlin.test.assertEquals

class UtilsKtTest {
    @Test
    fun cropImageNothing() {
        OpenCV.loadLocally()
        val img =
            "many-kills_en.png".toResourceMat(this::class.java.classLoader, IMREAD_GRAYSCALE)
        val cropped = img.cropImage(.15, 0.75, .15, .85)
        assertEquals(((0.75 - 0.15) * img.height().toDouble()).toInt(), cropped.height())
        assertEquals(((0.85 - 0.15) * img.width().toDouble()).toInt(), cropped.width())
        // HighGui.imshow("Cropped", cropped)
        // HighGui.waitKey(0)
        // HighGui.destroyAllWindows()
    }

    @Test
    fun cropNextBack() {
        OpenCV.loadLocally()
        val img =
            "many-kills_en.png".toResourceMat(this::class.java.classLoader, IMREAD_GRAYSCALE)
        val top = .85
        val bottom = 1.0
        val left = .45
        val right = .55
        val cropped = img.cropImage(top, bottom, left, right)
        assertEquals(((bottom - top) * img.height().toDouble()).toInt(), cropped.height())
        assertEquals(((right - left) * img.width().toDouble()).toInt(), cropped.width())
        // HighGui.imshow("Cropped", cropped)
        // Imgcodecs.imwrite("cropped.png", cropped)
        // HighGui.waitKey(0)
        // HighGui.destroyAllWindows()
    }
}
