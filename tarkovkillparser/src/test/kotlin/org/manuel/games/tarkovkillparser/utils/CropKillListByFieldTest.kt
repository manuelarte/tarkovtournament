package org.manuel.games.tarkovkillparser.utils

import nu.pattern.OpenCV
import org.junit.jupiter.api.Test
import org.manuel.games.tarkovkillparser.toResourceMat
import org.opencv.highgui.HighGui
import org.opencv.imgcodecs.Imgcodecs.IMREAD_GRAYSCALE
import kotlin.test.Ignore

class CropKillListByFieldTest {
    @Ignore("Test to be used to manually check the images")
    @Test
    fun parseKillEntryForFieldExample() {
        OpenCV.loadLocally()
        val img = "./en/many-kills_en_FOXP5SB.png".toResourceMat(this::class.java.classLoader, IMREAD_GRAYSCALE).bitwiseNot()
        val parser = CropKillListByField(img.cropPlayerKillsTable())
        for (n in 1..9) {
            KillEntryField.entries.forEach { field ->
                val croppedImage = parser.imgKillEntry(n).let { parser.imgKillEntryForField(it, field) }
                HighGui.imshow("$n - $field", croppedImage)
                HighGui.waitKey(0)
                HighGui.destroyAllWindows()
            }
        }
    }
}
