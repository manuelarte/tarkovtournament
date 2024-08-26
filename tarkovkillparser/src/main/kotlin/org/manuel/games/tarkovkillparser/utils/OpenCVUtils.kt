package org.manuel.games.tarkovkillparser.utils

import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.MatOfByte
import org.opencv.core.Rect
import org.opencv.imgcodecs.Imgcodecs
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.InputStream
import javax.imageio.ImageIO

/**
 * Crop image based on normalized values
 */
fun Mat.cropImage(
    top: Double,
    bottom: Double,
    left: Double,
    right: Double,
): Mat {
    check(this.channels() == 1) { "Img needs to be in grayscale" }
    require(top in 0.0..1.0) { "top ($top) must be between 0 and 1" }
    require(bottom in top..1.0) { "bottom ($bottom) must be between top and 1" }
    require(left in 0.0..1.0) { "left ($left) must be between 0 and 1" }
    require(right in left..1.0) { "right ($right) must be between left and 1" }

    // Convert normalized coordinates to pixel values
    val x = (left * this.cols()).toInt().coerceIn(0, this.width() - 1)
    val y = (top * this.rows()).toInt().coerceIn(0, this.height() - 1)
    val width = ((right - left) * this.cols()).toInt().coerceIn(1, this.width() - x)
    val height = ((bottom - top) * this.rows()).toInt().coerceIn(1, this.height() - y)

    return Mat(
        this.clone(),
        Rect(
            x,
            y,
            width,
            height,
        ),
    )
}

fun Mat.bitwiseNot(): Mat {
    val dst = Mat()
    Core.bitwise_not(this, dst)
    return dst
}

fun Mat.toBufferedImage(): BufferedImage {
    val matOfByte = MatOfByte()
    Imgcodecs.imencode(".jpg", this, matOfByte)
    val byteArray = matOfByte.toArray()
    val inputStream: InputStream = ByteArrayInputStream(byteArray)
    val bufImage = ImageIO.read(inputStream)
    return bufImage
}
