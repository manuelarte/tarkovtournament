package org.manuel.games.tarkovkillparser.utils

import org.opencv.core.Core
import org.opencv.core.DMatch
import org.opencv.core.Mat
import org.opencv.core.MatOfDMatch
import org.opencv.core.MatOfKeyPoint
import org.opencv.features2d.BFMatcher
import org.opencv.features2d.ORB
import org.opencv.imgcodecs.Imgcodecs

private const val NUMBER_WIDTH: Double = 0.05
private const val LOCATION_WIDTH: Double = 0.17
private const val TIME_WIDTH: Double = 0.12
private const val PLAYER_WIDTH: Double = 0.23
private const val LEVEL_WIDTH: Double = 0.04
private const val FACTION_WIDTH: Double = 0.115

fun isKillImage(
    baseImage: Mat = Imgcodecs.imread("./src/main/resources/base-image-kill-parser.png")!!,
    img: Mat,
): Boolean = areImgSimilar(baseImage, img, 50.0, 0.1)

fun areImgSimilar(
    baseImage: Mat,
    img: Mat,
    minimumDistance: Double = 50.0,
    threshold: Double = 0.1,
): Boolean {
    require(!baseImage.empty()) { "Base Image can't be empty" }
    require(!img.empty()) { "img can't be empty" }
    check(img.channels() == 1) { "img needs to be in grayscale" }
    ORB.create().also { orb ->
        // Detect keyPoints and compute descriptors
        val (_, descriptorsBaseImage) =
            Pair(MatOfKeyPoint(), Mat()).also { (keyPoints, descriptors) ->
                orb.detectAndCompute(baseImage, Mat(), keyPoints, descriptors)
            }
        val (
            _, descriptors,
        ) =
            Pair(MatOfKeyPoint(), Mat()).also { (keyPoints, descriptors) ->
                orb.detectAndCompute(img, Mat(), keyPoints, descriptors)
            }

        // Create a BFMatcher object with Hamming distance as measurement
        val bfMatcher = BFMatcher.create(Core.NORM_HAMMING, false)

        // Match descriptors
        val matches: MutableList<DMatch> =
            MatOfDMatch()
                .also {
                    bfMatcher.match(descriptorsBaseImage, descriptors, it)
                }.toList()
                .also {
                    it.sortWith { m1: DMatch, m2: DMatch ->
                        m1.distance.toDouble().compareTo(m2.distance.toDouble())
                    }
                }
        val goodMatches = matches.filter { it.distance > minimumDistance }
        val similarity = goodMatches.size.toDouble() / matches.size.toDouble()
        return similarity > threshold
    }
}

fun Mat.cropNextBack(): Mat {
    val top = .85
    val bottom = 1.0
    val left = .45
    val right = .55
    return this.cropImage(top, bottom, left, right)
}

fun Mat.cropRaidMetadata(): Mat {
    val top = .95
    val bottom = 1.0
    val left = .0
    val right = .25
    return this.cropImage(top, bottom, left, right)
}

fun Mat.cropPlayerKillsTable(): Mat {
    val top = .18
    val bottom = .73
    val left = .17
    val right = .83
    return this.cropImage(top, bottom, left, right)
}

enum class KillEntryField(
    val width: Double?,
    private val previousField: KillEntryField?,
) {
    NUMBER(NUMBER_WIDTH, null),
    LOCATION(LOCATION_WIDTH, NUMBER),
    TIME(TIME_WIDTH, LOCATION),
    PLAYER(PLAYER_WIDTH, TIME),
    LEVEL(LEVEL_WIDTH, PLAYER),
    FACTION(FACTION_WIDTH, LEVEL),
    STATUS(null, FACTION),
    ;

    fun getLeftRoi(): Double {
        if (this == NUMBER) return 0.0
        return this.previousField!!.width!! + this.previousField.getLeftRoi()
    }
}

class CropKillListByField(
    /** Cropped image with the table kills */
    private val croppedImage: Mat,
) {
    /** Normalized Height of a kill entry */
    private val killHeight: Double = 0.1

    /**
     * Get the kill number n of the player kill list
     */
    fun imgKillEntry(n: Int): Mat {
        require(n in 1..9) { "1 <= n <= 9" }
        return this.croppedImage.cropImage(n * this.killHeight, (n + 1) * this.killHeight, 0.0, 1.0)
    }

    /**
     * Get the image cropped for the corresponding field for that kill entry img
     */
    fun imgKillEntryForField(
        killEntry: Mat,
        field: KillEntryField,
    ): Mat {
        val left = field.getLeftRoi()
        val right = field.width?.let { left + it } ?: 1.0
        return killEntry.cropImage(0.0, 1.0, left, right)
    }
}
