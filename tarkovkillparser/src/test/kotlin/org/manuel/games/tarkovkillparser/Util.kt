package org.manuel.games.tarkovkillparser

import org.opencv.core.Mat
import org.opencv.core.MatOfByte
import org.opencv.imgcodecs.Imgcodecs

class Util

fun String.toResourceMat(
    classLoader: ClassLoader,
    flag: Int,
): Mat {
    val bytes =
        classLoader
            .getResource(this)!!
            .readBytes()
    return Imgcodecs.imdecode(MatOfByte(*bytes), flag)
}
