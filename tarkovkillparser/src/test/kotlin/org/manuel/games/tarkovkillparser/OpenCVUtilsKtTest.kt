package org.manuel.games.tarkovkillparser

import nu.pattern.OpenCV
import org.manuel.games.tarkovkillparser.utils.cropImage
import org.opencv.imgcodecs.Imgcodecs.IMREAD_GRAYSCALE
import kotlin.test.Test
import kotlin.test.assertEquals

class OpenCVUtilsKtTest {
    @Test
    fun cropImageNothing() {
        OpenCV.loadLocally()
        val img =
            "en/many-kills_en_FOXP5SB.png".toResourceMat(this::class.java.classLoader, IMREAD_GRAYSCALE)
        val cropped = img.cropImage(.15, 0.75, .15, .85)
        assertEquals(((0.75 - 0.15) * img.height().toDouble()).toInt(), cropped.height())
        assertEquals(((0.85 - 0.15) * img.width().toDouble()).toInt(), cropped.width())
    }

    @Test
    fun cropNextBack() {
        OpenCV.loadLocally()
        val img =
            "en/many-kills_en_FOXP5SB.png".toResourceMat(this::class.java.classLoader, IMREAD_GRAYSCALE)
        val top = .85
        val bottom = 1.0
        val left = .45
        val right = .55
        val cropped = img.cropImage(top, bottom, left, right)
        assertEquals(((bottom - top) * img.height().toDouble()).toInt(), cropped.height())
        assertEquals(((right - left) * img.width().toDouble()).toInt(), cropped.width())
    }
}
